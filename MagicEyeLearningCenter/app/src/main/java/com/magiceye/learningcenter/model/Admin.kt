package com.magiceye.learningcenter.model

import java.io.Serializable
import javax.annotation.Nullable

data class Admin(@Nullable var id: String?, @Nullable val name: String?, @Nullable val phone:String?,@Nullable val admin:Boolean?):Serializable {
    constructor():this(null,null,null,null)
    constructor(name: String?,phone: String?,isAdmin:Boolean?):this(null,name,phone,isAdmin)
}