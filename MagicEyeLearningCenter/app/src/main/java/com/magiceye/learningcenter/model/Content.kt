package com.magiceye.learningcenter.model

import java.io.Serializable

data class Content(var id:String?, val name:String?, val visible:Boolean?):Serializable {
    constructor():this(null,null,null)
    constructor(name: String?,visible: Boolean?):this(null,name,visible)
}