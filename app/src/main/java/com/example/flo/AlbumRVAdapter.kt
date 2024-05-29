package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    // 아이템 클릭과 아이템 제거 이벤트를 처리하는 메소드를 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    // 아이템 클릭 리스너를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    // 외부에서 아이템 클릭 리스너를 설정할 수 있도록 하는 메소드
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 앨범을 리스트에 추가하고 리사이클러뷰를 갱신
    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged() // 데이터 조작 시 리사이클러뷰는 데이터가 변경된지 모르기 때문에 notifyDataSetChanged() 함수를 호출함.
    }

    // 특정 위치의 앨범을 리스트에서 제거하고 리사이클러뷰를 갱신
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더 객체를 생성하고 초기화할 때 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    // 뷰홀더 객체와 데이터를 바인딩할 때 호출
    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position]) // 현재 위치의 앨범 데이터를 뷰홀더에 바인딩
        holder.itemView.setOnClickListener{
            mItemClickListener.onItemClick(albumList[position]) } // 클릭 시 인터페이스 메소드 호출
    }

    // 아이템의 총 개수를 반환
    override fun getItemCount(): Int = albumList.size

    // 뷰 홀더 클래스 : 리사이클러뷰의 각 아이템을 보유
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        // 앨범 데이터를 뷰에 바인딩하는 메소드
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.title
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }

}