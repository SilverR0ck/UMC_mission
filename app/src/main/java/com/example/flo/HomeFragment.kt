package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 더미데이터 추가
        albumDatas.apply {
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연 (Tae Yeon", R.drawable.img_album_exp6))
        }

        // 리사이클러뷰 어댑터 생성 및 설정
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        // 리사이클러뷰의 레이아웃 매니저 설정 (수평 스크롤)
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 아이템 클릭 리스너 설정
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            // 아이템 클릭 시 앨범 프래그먼트로 전환
            override fun onItemClick(album: Album) { changeAlbumFragment(album) }
            // 아이템 제거
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })


        // 배너 어댑터 생성 및 설정
        val bannerAdapter = BannerVPAdapter(this)
        // 배너 프래그먼트 추가
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        // 배너의 방향을 수평으로 설정
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    // 앨범 프로그먼트로 전환하는 메소드
    private fun changeAlbumFragment(album: Album){
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment().apply {
                        arguments = Bundle().apply {
                            // 앨범 객체를 JSON 문자열로 변환하여 번들에 추가
                            val gson = Gson()
                            val albumJson = gson.toJson(album)
                            putString("album", albumJson)
                        }
                    })
                    .commitAllowingStateLoss()
    }


}