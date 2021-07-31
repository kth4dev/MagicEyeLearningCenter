package com.magiceye.admin.model

import java.io.Serializable
import javax.annotation.Nullable


data class Link(@Nullable var id: String?,@Nullable val name: String?, @Nullable val link: String?, @Nullable val visible:Boolean?):
    Serializable {
    constructor():this(null,null,null,null)
    constructor(name: String?,link: String?,visible: Boolean?):this(null,name,link,visible)
}