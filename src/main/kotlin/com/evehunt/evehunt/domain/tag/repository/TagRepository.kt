package com.evehunt.evehunt.domain.tag.repository

import com.evehunt.evehunt.domain.tag.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long>, QueryDslTagRepository{

}