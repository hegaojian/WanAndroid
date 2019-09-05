package me.hegj.wandroid.app.utils

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.layout_fotter_loadmore.view.*
import kotlinx.android.synthetic.main.layout_loading.view.*
import me.hegj.wandroid.app.weight.DefineLoadMoreView

class RecyclerViewUtils {

    fun  initRecyclerView(context:Context,recyclerview:SwipeRecyclerView,loadmoreListener: SwipeRecyclerView.LoadMoreListener):DefineLoadMoreView{
        val footerView = DefineLoadMoreView(context)
        recyclerview.addFooterView(footerView)
        recyclerview.setLoadMoreView(footerView)//添加加载更多尾部
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.setHasFixedSize(true)
        recyclerview.setLoadMoreListener(loadmoreListener)//设置加载更多回调
        footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            //设置尾部点击回调
            footerView.onLoading()
            loadmoreListener.onLoadMore()
        })
        return footerView
    }

}