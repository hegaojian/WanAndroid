package me.hegj.wandroid.app.event

class AddEvent() : BaseEvent(){
    var code = TODO_CODE

    constructor(code: Int) : this() {
        this.code = code
    }

    companion object{
        //添加清单
        val TODO_CODE = 1
        //分享文章
        val SHARE_CODE = 2
        //删除文章
        val DELETE_CODE = 3
    }

}

