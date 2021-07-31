package com.magiceye.learningcenter.model

import java.io.Serializable
import javax.annotation.Nullable

data class Student( @Nullable val name: String?, @Nullable val phone:String?):Serializable {
    constructor():this(null,null)
}