package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;
import org.jeecg.modules.im.mapper.MucMemberLevelCtgMapper;
import org.jeecg.modules.im.service.MucMemberLevelCtgService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 群组用户等级分类 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class MucMemberLevelCtgServiceImpl extends BaseServiceImpl<MucMemberLevelCtgMapper, MucMemberLevelCtg> implements MucMemberLevelCtgService {
    @Autowired
    private MucMemberLevelCtgMapper mucMemberLevelCtgMapper;

    @Override
    public Result<Object> createOrUpdate(MucMemberLevelCtg ctg) {
        if(ctg.getId()!=null){
            return success(updateById(ctg));
        }
        return success(save(ctg));
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mucMemberLevelCtgMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
}
