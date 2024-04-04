package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Font;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.Font;

import java.util.List;

/**
 * <p>
 * 应用字体 服务类
 * </p>
 *
 * @author junko
 * @since 2024-02-12
 */
public interface FontService extends IService<Font> {
    Result<Object> findAll();

    Result<Object> createOrUpdate(Font font);
    Result<Object> del(String ids);

    List<Font> queryLogicDeleted();
    List<Font> queryLogicDeleted(LambdaQueryWrapper<Font> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
