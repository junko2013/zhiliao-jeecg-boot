package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Locale;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 语言包 服务类
 * </p>
 *
 * @author junko
 * @since 2023-12-18
 */
public interface ILocaleService extends IService<Locale> {
    List<Locale> findAll();

    String getContent(int id);


    List<Locale> queryLogicDeleted();
    List<Locale> queryLogicDeleted(LambdaQueryWrapper<Locale> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
