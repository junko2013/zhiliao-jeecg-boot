//package org.jeecg.modules.im.xxljob;
//
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.log.XxlJobLogger;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Date;
//
///**
// * 清理调度日志
// */
//@Slf4j
//public class ClearJobLogHandler extends IJobHandler {
//
//    @Override
//    public ReturnT<String> execute(String param) throws Exception {
//        int count = Db.use("job").delete("delete from xxl_job_log where trigger_time <= ?", ToolDateTime.getDate(ToolDateTime.getDateByDatePlusDays(new Date(),-2),ToolDateTime.pattern_ymd_hms));
//        XxlJobLogger.log("清理调度日志={}",count);
//        return SUCCESS;
//    }
//}
