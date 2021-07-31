package com.magiceye.admin.model

import java.io.Serializable
import javax.annotation.Nullable

data class Course(@Nullable var id: String?, @Nullable val name: String?, @Nullable val visible:Boolean?):Serializable {
    constructor():this(null,null,null)
    constructor(name: String?,visible: Boolean?):this(null,name,visible)
}