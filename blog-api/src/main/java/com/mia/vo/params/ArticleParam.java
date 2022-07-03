package com.mia.vo.params;

import com.mia.vo.CategoryVo;
import com.mia.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @Author GuoDingWei
 * @Date 2022/5/11 22:31
 */


/**
 * 盛装rpcservice层的子功能点方法的复合型入参，即rpc服务的复合型入参。此时原则上可以随意扩展其属性
 * 盛装调用外部接口的复合型入参。此时原则上不可以随意扩展其属性，因为需要对应外部接口的文档
 */

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}

