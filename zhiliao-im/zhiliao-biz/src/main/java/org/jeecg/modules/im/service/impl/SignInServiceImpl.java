package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QCoinBill;
import org.jeecg.modules.im.entity.query_helper.QSignIn;
import org.jeecg.modules.im.mapper.SignInMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 签到 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Service
@Slf4j
public class SignInServiceImpl extends BaseServiceImpl<SignInMapper, SignIn> implements ISignInService {
    @Autowired
    private SignInMapper signInMapper;
    @Resource
    private IServerConfigService serverConfigService;
    @Resource
    private IUserService IUserService;
    @Resource
    private IUserInfoService IUserInfoService;
    @Resource
    private ICoinBillService ICoinBillService;

    @Override
    public IPage<SignIn> pagination(MyPage<SignIn> page, QSignIn q) {
        return signInMapper.pagination(page,q);
    }

    @Override
    public SignIn findByDateOfUser(String date, Integer userId) {
        return signInMapper.findByDateOfUser(date,userId);
    }


    //当前的签到状态、累计签到天数、今天是否已签到，以及签到可以获得的金币数量
    @Override
    public Result<Object> info(Integer userId) {
        User user = IUserService.findById(userId);
        UserInfo userInfo = IUserInfoService.findBasicByUserId(user.getId());
        ServerConfig serverConfig = getServerConfig();
        Kv kv = Kv.create();
        String today = ToolDateTime.getDate(new Date(),ToolDateTime.pattern_ymd);
        SignIn signInToday = findByDateOfUser(today, user.getId());
        //今日已签到
        kv.set("today",signInToday);
        //连续签到天数
        kv.set("continueDays",getContinueSignDays(userId));
        //累计签到
        kv.set("totalDays",getTotalDays(userId));
        //当前月份
        kv.set("month",ToolDateTime.getDate(ToolDateTime.pattern_ym));
        //连续签到奖励
        kv.set("rewardsContinue",serverConfig.getSignInRewardContinue());
        //开启连续签到
        kv.set("enableContinue",serverConfig.getSignInContinueDay() > 0);
        kv.set("canMakeupDays",serverConfig.getSignInMakeupDays());
        kv.set("note",serverConfig.getSignInNote());
        QCoinBill q = new QCoinBill();
        List<String> types = new ArrayList<>();
        types.add(CoinBill.Type.signIn.name());
        types.add(CoinBill.Type.signInContinue.name());
        q.setUserId(userId);
        q.setTypes(types);
        kv.set("coin", ICoinBillService.getAmount(q));
        types.clear();
        types.add(CoinBill.Type.makeup.name());
        q.setTypes(types);
        kv.set("cost", ICoinBillService.getAmount(q));
        //查询用户近n天的签到日期
        kv.set("signInInfo", getSignDatesInfo(userId));
        kv.set("myCoins",userInfo.getCoin());
        return success(kv);
    }
    //获取用户当前连续签到天数
    private int getContinueSignDays(int userId){
        String today = ToolDateTime.getDate(new Date(),ToolDateTime.pattern_ymd);
        SignIn signInToday = findByDateOfUser(today, userId);
        String yesterday = ToolDateTime.getDate(ToolDateTime.getDateByDatePlusDays(new Date(),-1),ToolDateTime.pattern_ymd);
        SignIn signInYesterday = findByDateOfUser(yesterday, userId);
        if(signInToday!=null){
            return signInToday.getDays();
        }
        if(signInYesterday!=null){
            return signInYesterday.getDays();
        }
        return 0;
    }

    @Override
    public Kv getSignDatesInfo(Integer userId) {
        try {
            ServerConfig serverConfig = getServerConfig();
            //已签到的日期
            //判断今日距离本月第一天的天数间隔
            int daySpace;
            //最大不超过31天
            daySpace = ToolDateTime.getDateDaySpace(ToolDateTime.getMonthFirstDay(new Date()), new Date())+1;
            if (daySpace < serverConfig.getSignInMakeupDays()) {
                //但配置的最大可补签天数可能超过31天
                daySpace = serverConfig.getSignInMakeupDays();
            }
            //如果获取的第一天是上个月的，则+上个月的天数
            Date firstDate = ToolDateTime.getDateByDatePlusDays(new Date(), -1 * daySpace);

            LocalDate startLocalDate = ToolDateTime.convertToLocalDateViaInstant(firstDate);
            LocalDate endLocalDate = ToolDateTime.convertToLocalDateViaInstant(new Date());


            int monthsBetween = endLocalDate.getMonthValue() - startLocalDate.getMonthValue()
                    + (endLocalDate.getYear() - startLocalDate.getYear()) * 12;

            // 检查日是否需要调整月份计数
            if (endLocalDate.getDayOfMonth() >= startLocalDate.getDayOfMonth()) {
                monthsBetween++;
            }
            if(monthsBetween > 0){
                while (monthsBetween-- > 1) {
                    daySpace += ToolDateTime.getDateDaySpace(ToolDateTime.getMonthFirstDay(firstDate), firstDate)+1;
                    //循环移至上个月的最后一天
                    firstDate = ToolDateTime.getDateByDatePlusDays(firstDate,-1);
                }
            }
            List<Date> signInDates = signInMapper.findSignInsWithinDays(userId, daySpace);
            List<Date> allDates = ToolDateTime.getDatesBefore(new Date(), daySpace);
            Kv data = Kv.create();
            List<Kv> dates = new ArrayList<>();
            List<String> costs = Arrays.asList(StringUtils.split(serverConfig.getSignInMakeupCost(), ","));
            int missing = 0;
            int signed = 0;
            //最早可补签的日期
            //先根据最多可补签天数查询
            //如果用户的最早签到日期在此之前，
            Date minCanSignDate = ToolDateTime.getStartTimeOfDate(ToolDateTime.getDateByDatePlusDays(new Date(), -1 * serverConfig.getSignInMakeupDays()));
            String defaultCost = costs.get(costs.size() - 1);
            if (!signInDates.isEmpty() && !allDates.isEmpty()) {
                int canNotCount = 0;
                //得到不能签到的天数
                for (Date date : allDates) {
                    //判断该日期是否比最小的可签到日期小
                    if (ToolDateTime.compare(date, minCanSignDate) < 0) {
                        canNotCount++;
                    }
                }
                // 确定costs列表中最高的金币数作为超出部分的默认补签花费
                //连续签到的第一天
                Date firstOfContinue = null;
                for (int i = 0; i < allDates.size(); i++) {
                    Date date = allDates.get(i);
                    SignIn signIn = findByDateOfUser(ToolDateTime.getDate(date, ToolDateTime.pattern_ymd), userId);
                    if(firstOfContinue==null&&signIn==null&&i>0){
                        firstOfContinue = allDates.get(i-1);
                    }
                    //该日期已签到
                    if (signInDates.contains(date)) {
//                        if(ToolDateTime.compare(date,minCanSignDate)>=0) {
                        if(i<=signInDates.size()) {
                            signed++;
                            dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("signIn", signIn));
                        }else{
                            dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("canNotSign",1).set("signIn", signIn));
                        }
                    } else {
                        //判断该日期是否比最小的可签到日期大
                        //漏签的
                        if (ToolDateTime.compare(date, minCanSignDate) >= 0) {
                            missing++;
                            //该日期未签到
                            // 如果日期索引小于costs列表长度，则按索引对应，否则使用默认补签花费
                            //连续签到的第一天，而不是连续签到的最后一天
                            int days = daySpace-1-canNotCount-ToolDateTime.getDateDaySpace(minCanSignDate, date);
                            //-1是因为今天的已经处理过
                            String cost = days - 1 < costs.size() ? days - 1 < 0 ? costs.get(0) : costs.get(days - 1) : defaultCost;
                            dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("cost", Integer.parseInt(cost)));
                        }else{
                            //不能签的
                            dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("canNotSign", 1));
                        }
                    }
                }
            }else{
                //可签到的日期中没有任何签到日期
                allDates = ToolDateTime.getDatesBefore(new Date(), daySpace);
                int canNotCount = 0;
                //得到不能签到的天数
                for (Date date : allDates) {
                    //今天
                    if(ToolDateTime.isSameDay(date,new Date())){
                        dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("cost", 0));
                        continue;
                    }
                    //判断该日期是否比最小的可签到日期小
                    if (ToolDateTime.compare(date, minCanSignDate) < 0) {
                        canNotCount++;
                    }
                }
                for (Date date : allDates) {
                    //今天上面已经添加过
                    if(ToolDateTime.isSameDay(date,new Date())){
                        continue;
                    }
                    //判断该日期是否不小于最小的可签到日期，则是可签的，且是漏签
                    //漏签的
                    if (ToolDateTime.compare(date, minCanSignDate) >= 0) {
                        missing++;
                        //该日期未签到
                        // 如果日期索引小于costs列表长度，则按索引对应，否则使用默认补签花费
                        //连续签到的第一天，而不是连续签到的最后一天
                        int days = daySpace-1-canNotCount-ToolDateTime.getDateDaySpace(minCanSignDate, date);
                        //-1是因为今天的已经处理过
                        String cost = days - 1 < costs.size() ? days - 1 < 0 ? costs.get(0) : costs.get(days - 1) : defaultCost;
                        dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("cost", Integer.parseInt(cost)));
                    }else{
                        //不能签的
                        dates.add(Kv.by("date", ToolDateTime.getDate(date, ToolDateTime.pattern_ymd)).set("canNotSign", 1));
                    }
                }
            }
            data.set("missing", missing);
            data.set("signed", signed);
            data.set("dates", dates);
            return data;
        }catch (Exception e){
            log.error("查询签到信息异常",e);
            return null;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> sign(Integer userId) {
        User user = IUserService.findById(userId);
        ServerConfig serverConfig = getServerConfig();
        if (serverConfig.getEnableSignIn()) {
            return fail("签到功能未开启");
        }
        try {
            UserInfo userInfo = IUserInfoService.findBasicByUserId(user.getId());
            if (userInfo.getSignInDay() >= serverConfig.getSignInMaxDay() && serverConfig.getSignInMaxDay() > 0) {
                return fail("已达到累计签到天数，请申请提现后再签到！");
            }
            //今日签到
            String today = ToolDateTime.getDate(new Date(),ToolDateTime.pattern_ymd);
            if (findByDateOfUser(today, user.getId()) != null) {
                return fail("今日已签到");
            }
            //昨日签到
            String yesterday = ToolDateTime.getDate(ToolDateTime.getYesterday(),ToolDateTime.pattern_ymd);
            SignIn yesterdaySignIn = signInMapper.findByDateOfUser(yesterday,userId);
            //连续签到天数以昨日签到记录为准
            int continueDays = 0;
            if(yesterdaySignIn!=null){
                continueDays = yesterdaySignIn.getDays();
            }
            String rewardContinueConfig = serverConfig.getSignInRewardContinue();
            //额外奖励
            int rewardContinue;
            //开启连续签到
            if (serverConfig.getSignInContinueDay() > 0) {
                //[1,2,3]
                String[] rewards = StringUtils.split(rewardContinueConfig, ",");
                //判断设置的个数是否小于当前连续签到天数 只设置了3个，那么当前连续签到超过3天时，则取最后一个
                if(rewards.length<=continueDays){
                    rewardContinue = Integer.parseInt(rewards[rewards.length-1]);
                }else {
                    rewardContinue = Integer.parseInt(rewards[continueDays]);
                }
            } else {
                rewardContinue = Integer.parseInt(rewardContinueConfig);
            }
            SignIn signIn = new SignIn();
            signIn.setUserId(user.getId());
            signIn.setDate(ToolDateTime.getDate(ToolDateTime.pattern_ymd));
            signIn.setTsCreate(getTs());
            signIn.setDate(ToolDateTime.getDate(new Date(),ToolDateTime.pattern_ymd));
            //连续签到天数
            signIn.setDays(yesterdaySignIn==null?1:yesterdaySignIn.getDays()+1);
            signIn.setRewardContinue(rewardContinue);
            signIn.setReward(serverConfig.getSignInReward());
            signIn.setServerId(user.getServerId());
            if(!save(signIn)){
                return fail();
            }
            userInfo.setSignInDay(userInfo.getSignInDay()+1);
            //添加账单记录
            if(signIn.getReward()>0){
                CoinBill bill = new CoinBill();
                bill.setAmount(signIn.getReward());
                bill.setIsIncrease(true);
                bill.setReason("每日签到奖励");
                bill.setBalanceBefore(userInfo.getCoin());
                bill.setBalanceAfter(userInfo.getCoin()+bill.getAmount());
                bill.setTsCreate(getDate());
                bill.setType(CoinBill.Type.signIn.getCode());
                bill.setUserId(userId);
                if(!ICoinBillService.save(bill)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fail();
                }
                userInfo.setCoin(userInfo.getCoin()+signIn.getReward());
            }
            if(signIn.getRewardContinue()>0){
                CoinBill bill = new CoinBill();
                bill.setAmount(signIn.getRewardContinue());
                bill.setIsIncrease(true);
                bill.setReason("连续签到奖励");
                bill.setBalanceBefore(userInfo.getCoin());
                bill.setBalanceAfter(userInfo.getCoin()+bill.getAmount());
                bill.setTsCreate(getDate());
                bill.setType(CoinBill.Type.signInContinue.getCode());
                bill.setUserId(userId);
                if(!ICoinBillService.save(bill)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fail();
                }
                userInfo.setCoin(userInfo.getCoin()+signIn.getRewardContinue());
            }
            if(!IUserInfoService.updateById(userInfo)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return fail();
            }
            Kv data = Kv.create();
            data.set("month",ToolDateTime.getCurrentMonth());
            data.set("day",ToolDateTime.getCurrentDay());
            data.set("reward",signIn.getReward()+signIn.getRewardContinue());
            data.set("days",signIn.getDays());
            data.set("info",info(userId).getResult());
            return success(data);
        }catch (Exception e){
            log.error("签到异常：user={}", user, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> makeup(String date, Integer userId) {
        User user = IUserService.findById(userId);
        ServerConfig serverConfig = getServerConfig();
        if (serverConfig.getEnableSignIn()) {
            return fail("签到功能未开启");
        }
        //判断日期是否可补签
        Date minCanSignDate = ToolDateTime.getStartTimeOfDate(ToolDateTime.getDateByDatePlusDays(new Date(), -1 * serverConfig.getSignInMakeupDays()));
        Date dt = ToolDateTime.getDate(date,ToolDateTime.pattern_ymd);
        if(ToolDateTime.compare(dt,minCanSignDate)<0){
            return fail("该日期不可补签");
        }
        if(findByDateOfUser(date,userId)!=null){
            return fail("不可重复签到");
        }
        try{
            UserInfo userInfo = IUserInfoService.findBasicByUserId(user.getId());
            if (userInfo.getSignInDay() >= serverConfig.getSignInMaxDay() && serverConfig.getSignInMaxDay() > 0) {
                return fail("已达到累计签到天数，请申请提现后再签到！");
            }
            //补签扣费
            List<String> costs = Arrays.asList(StringUtils.split(serverConfig.getSignInMakeupCost(), ","));
            String defaultCost = costs.get(costs.size() - 1);
            int days = serverConfig.getSignInMakeupDays()-ToolDateTime.getDateDaySpace(minCanSignDate,dt);
            Integer cost = Integer.parseInt(days-1 < costs.size() ?days-1<0?costs.get(0):costs.get(days-1) : defaultCost);
            if(userInfo.getCoin()<cost){
                return fail("金币不足");
            }

            SignIn signIn = new SignIn();
            signIn.setUserId(user.getId());
            signIn.setDate(date);
            signIn.setTsCreate(getTs());

            signIn.setMakeupCost(cost);
            signIn.setIsMakeup(true);
            //连续签到天数
            //前一天签到
            String preDay = ToolDateTime.getDate(ToolDateTime.getDateByDatePlusDays(dt,-1),ToolDateTime.pattern_ymd);
            SignIn preDaySignIn = signInMapper.findByDateOfUser(preDay,userId);
            signIn.setDays(preDaySignIn==null?1:preDaySignIn.getDays()+1);
            signIn.setRewardContinue(0);
            signIn.setReward(serverConfig.getSignInMakeupReward()?serverConfig.getSignInReward():0);
            signIn.setServerId(user.getServerId());
            if(!save(signIn)){
                return fail();
            }
            userInfo.setSignInDay(userInfo.getSignInDay()+1);
            //添加账单记录
            if(signIn.getMakeupCost()>0){
                CoinBill bill = new CoinBill();
                bill.setAmount(signIn.getMakeupCost());
                bill.setIsIncrease(false);
                bill.setReason("补签扣费");
                bill.setBalanceBefore(userInfo.getCoin());
                bill.setBalanceAfter(userInfo.getCoin()-bill.getAmount());
                bill.setTsCreate(getDate());
                bill.setType(CoinBill.Type.makeup.getCode());
                bill.setUserId(userId);
                if(!ICoinBillService.save(bill)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fail();
                }
                userInfo.setCoin(userInfo.getCoin()-signIn.getMakeupCost());
            }
            if(signIn.getReward()>0){
                CoinBill bill = new CoinBill();
                bill.setAmount(signIn.getReward());
                bill.setIsIncrease(true);
                bill.setReason("每日签到奖励");
                bill.setBalanceBefore(userInfo.getCoin());
                bill.setBalanceAfter(userInfo.getCoin()+bill.getAmount());
                bill.setTsCreate(getDate());
                bill.setType(CoinBill.Type.signIn.getCode());
                bill.setUserId(userId);
                if(!ICoinBillService.save(bill)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fail();
                }
                userInfo.setCoin(userInfo.getCoin()+signIn.getReward());
            }
            if(!IUserInfoService.updateById(userInfo)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return fail();
            }
            //检查最早可签到最新可签之间是否存在未签，如果不存在未签，则将最新已签天数改为最早到最新的天数间隔+1
            SignIn latestSignIn = null;
            //今日签到
            String today = ToolDateTime.getDate(new Date(),ToolDateTime.pattern_ymd);
            SignIn todaySignIn = findByDateOfUser(today, user.getId());
            if (todaySignIn != null) {
                latestSignIn = todaySignIn;
            }
            if(latestSignIn==null){
                //昨日签到
                String yesterday = ToolDateTime.getDate(ToolDateTime.getYesterday(),ToolDateTime.pattern_ymd);
                SignIn yesterdaySignIn = signInMapper.findByDateOfUser(yesterday,userId);
                if (yesterdaySignIn != null) {
                    latestSignIn = yesterdaySignIn;
                }
            }
            if(latestSignIn!=null){
                //可补签范围内没有漏签
                if(getMissing(ToolDateTime.getDate(minCanSignDate,ToolDateTime.pattern_ymd),latestSignIn.getDate(),userId)==0){
                    latestSignIn.setDays(1+ToolDateTime.getDateDaySpace(ToolDateTime.getStartTimeOfDate(minCanSignDate),ToolDateTime.getDate(latestSignIn.getDate(),ToolDateTime.pattern_ymd)));
                    if(!updateById(latestSignIn)){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return fail();
                    }
                }
                //本日与最后签到日期直接没有漏签
                else if(getMissing(ToolDateTime.getDate(dt,ToolDateTime.pattern_ymd),latestSignIn.getDate(),userId)==0){
                    latestSignIn.setDays(1+ToolDateTime.getDateDaySpace(ToolDateTime.getStartTimeOfDate(dt),ToolDateTime.getDate(latestSignIn.getDate(),ToolDateTime.pattern_ymd)));
                    if(!updateById(latestSignIn)){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return fail();
                    }
                }
                //本日与最后签到日期直接没有漏签
                else if(getMissing(ToolDateTime.getDate(dt,ToolDateTime.pattern_ymd),latestSignIn.getDate(),userId)==0){
                    latestSignIn.setDays(1+ToolDateTime.getDateDaySpace(ToolDateTime.getStartTimeOfDate(dt),ToolDateTime.getDate(latestSignIn.getDate(),ToolDateTime.pattern_ymd)));
                    if(!updateById(latestSignIn)){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return fail();
                    }
                }
            }
            Kv data = Kv.create();
            data.set("info",info(userId).getResult());
            data.set("reward",signIn.getReward()-cost);
            return success(data);
        }catch (Exception e){
            log.error("补签异常：date={},userId={}", date,userId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail();
        }
    }

    @Override
    public Integer getTotalDays(Integer userId) {
        return signInMapper.getTotalDays(userId);
    }
    //获取两个日期之间未签的天数
    public Integer getMissing(String beginDate,String endDate,Integer userId){
        int days = 1+ToolDateTime.getDateDaySpace(ToolDateTime.getDate(beginDate,ToolDateTime.pattern_ymd),ToolDateTime.getDate(endDate,ToolDateTime.pattern_ymd));
        //总天数-签到天数
        return days - signInMapper.getSigned(beginDate,endDate,userId);
    }
}
