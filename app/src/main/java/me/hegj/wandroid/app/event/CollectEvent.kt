package me.hegj.wandroid.app.event


class CollectEvent :BaseEvent{
    var collect = false //需要改变的值
    var id = 0 //收藏变化的id
    var tag = ""
    constructor(collect: Boolean, id: Int) : super() {
        this.collect = collect
        this.id = id
    }

    constructor(collect: Boolean, id: Int, tag: String) : super() {
        this.collect = collect
        this.id = id
        this.tag = tag
    }

}