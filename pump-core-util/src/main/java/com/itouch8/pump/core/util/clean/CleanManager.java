package com.itouch8.pump.core.util.clean;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.env.PumpVersion;
import com.itouch8.pump.core.util.logger.CommonLogger;


public class CleanManager {

    
    private static final AtomicBoolean initMonitor = new AtomicBoolean(false);

    
    private static final String pumpScanPackage = "com.itouch8.pump";

    private static final SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    private static final String JOB_GROUP_NAME = "PUMP_CLEAN_JOB_GROUP";

    private static final String TRIGGER_GROUP_NAME = "PUMP_CLEAN_TRIGGER_GROUP";

    
    public static void initialize() {
        if (!initMonitor.get()) {
            synchronized (initMonitor) {
                if (!initMonitor.get()) {
                    CommonLogger.info(PumpVersion.Version + " celean job start...");
                    try {
                        init();
                    } catch (Exception e) {
                    }
                    initMonitor.set(true);
                }
            }
        } else {
        }
    }

    
    @SuppressWarnings("unchecked")
    private static void init() throws Exception {
        String scanPackage = BaseConfig.getInitScanPackage();
        if (CoreUtils.isBlank(scanPackage) || scanPackage.equals(pumpScanPackage)) {
            scanPackage = BaseConfig.getScanPackage();
        }
        if (CoreUtils.isBlank(scanPackage)) {
            scanPackage = pumpScanPackage;
        } else if (!scanPackage.equals(pumpScanPackage)) {
            scanPackage += "," + pumpScanPackage;
        }
        CommonLogger.debug("Pump @clean scan package is " + scanPackage);
        // 扫描含初始化注解的类
        Set<Class<?>> init = CoreUtils.scanClasses(scanPackage, Clean.class);
        if (null != init) {
            for (Class<?> r : init) {
                if (checkCls(r)) {
                    Class<? extends IJob> j = (Class<? extends IJob>) r;
                    addJob(j);
                }
            }

        }
    }

    private static void addJob(Class<? extends IJob> cls) throws Exception {
        Clean clean = cls.getAnnotation(Clean.class);
        if (null != clean) {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(clean.description(), JOB_GROUP_NAME).build();
            CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(clean.cron());
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(clean.description(), TRIGGER_GROUP_NAME).withSchedule(builder).build();
            sched.scheduleJob(jobDetail, trigger);
            sched.start();
        }
    }

    private static boolean checkCls(Class<?> r) {
        Class<?>[] interfaces = r.getInterfaces();
        if (null != interfaces && interfaces.length > 0) {
            for (Class<?> cls : interfaces) {
                if (cls.isAssignableFrom(IJob.class) || cls.isAssignableFrom(Job.class)) {
                    return true;
                }
            }
        }
        return false;
    }
}
