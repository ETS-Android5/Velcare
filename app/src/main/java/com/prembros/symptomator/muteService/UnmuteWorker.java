package com.prembros.symptomator.muteService;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.prembros.symptomator.MuteJob;
import com.prembros.symptomator.db.JobsViewModel;
import com.prembros.symptomator.utils.CalendarUtils;
import com.prembros.symptomator.utils.ClientConfig;
import com.prembros.symptomator.utils.NotificationsUtils;

import static android.content.Context.AUDIO_SERVICE;

public class UnmuteWorker extends Worker {

    private static final String TAG = UnmuteWorker.class.getSimpleName();
    private MuteJob job;

    public UnmuteWorker(@NonNull Context context,
                        @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Override this method to do your actual background processing.
     */
    @NonNull
    @Override
    public Result doWork() {

        Log.i(TAG,"start the unmute work at " + CalendarUtils.getPrettyHour(System.currentTimeMillis()));

        Context applicationContext = getApplicationContext();

        String jobid = getInputData().getString(MuteJobsModel.JOBID);
        JobsViewModel viewModel = new JobsViewModel((Application)applicationContext);
        job = viewModel.getJobById(jobid);

        try {

            if(MuteJobsModel.shouldWorkToday(job,MuteJobsModel.WORK_UNMUTE)) {
                Log.i(TAG,"Unmute should work today");
                AudioManager audioManager = (AudioManager) applicationContext.getSystemService(AUDIO_SERVICE);
                if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    NotificationsUtils.vibratePhone(getApplicationContext());
                } else {
                    Log.i(TAG,"Phone is already unmuted");
                }
            }

            //Prepare next Jobs if needed
            if(job.getRepeatMode() == MuteJob.MODE_REPEAT && job.getIsFirstTimeEnd() == 0) {
                Log.i(TAG,"Preparing next unmutes");
                job.setIsFirstTimeEnd(1);
                viewModel.update(job);
                MuteJobsModel.prepareDailyJobs(MuteJobsModel.WORK_MUTE, job);
            }

            NotificationsUtils.sendMuteNotification(getApplicationContext(),job,"unmute");
            Log.i(TAG, "Success with unmute");
            if(job.getRepeatMode() == MuteJob.MODE_ONE_TIME || job.isBusiness()) {
                ClientConfig clientConfig = new ClientConfig(applicationContext);
                if(clientConfig.isDeleteFinishJobs()) {
                    viewModel.delete(job);
                }
            }
            return Result.SUCCESS;

        } catch (Throwable throwable) {
            Log.i(TAG,throwable.getMessage());
            return Result.FAILURE;
        }
    }
}
