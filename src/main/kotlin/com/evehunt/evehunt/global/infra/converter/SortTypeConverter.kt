package com.evehunt.evehunt.global.infra.converter

import com.evehunt.evehunt.global.common.page.SortType
import org.springframework.core.convert.converter.Converter

class SortTypeConverter: Converter<String, SortType> {
    override fun convert(source: String): SortType {
        return SortType.of(source)
    }
}