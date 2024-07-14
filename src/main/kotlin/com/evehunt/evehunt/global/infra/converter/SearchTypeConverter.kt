package com.evehunt.evehunt.global.infra.converter

import com.evehunt.evehunt.global.common.page.SearchType
import org.springframework.core.convert.converter.Converter

class SearchTypeConverter : Converter<String, SearchType> {
    override fun convert(source: String): SearchType {
        return SearchType.of(source)
    }
}