package com.evehunt.evehunt.domain.image.model

import com.evehunt.evehunt.global.common.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class Image(
    @Id
    var uuid: String = UUID.randomUUID().toString(),
    private var filename: String = ""
): BaseTimeEntity()
{
    fun getLink(): String
    {
        return uuid + "_" + filename
    }
    companion object
    {
        fun from(imageName: String?): Image?
        {
            imageName ?: return null
            val imageNameArray = imageName.split("_")
                .also { if(it.size < 2) throw RuntimeException() }
            return Image(imageNameArray[0], imageNameArray[1])
        }
    }
}