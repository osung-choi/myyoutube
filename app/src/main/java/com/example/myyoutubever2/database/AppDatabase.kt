package com.example.myyoutubever2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myyoutubever2.database.dao.SubscribeDAO
import com.example.myyoutubever2.database.dao.UserDAO
import com.example.myyoutubever2.database.dao.VideoDAO
import com.example.myyoutubever2.database.entity.SubscribeDB
import com.example.myyoutubever2.database.entity.UserDB
import com.example.myyoutubever2.database.entity.VideoDB

@Database(entities = [UserDB::class, SubscribeDB::class, VideoDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun subscribeDao(): SubscribeDAO
    abstract fun videoDao(): VideoDAO

    companion object {
        private const val DB_NAME = "MyYoutubeDB"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase = synchronized(this) {
            return INSTANCE!!
        }

        fun createDatebase(context: Context) {
            INSTANCE = buildDatabase(context)
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val userDBList = arrayListOf<UserDB>().apply {
                            add(UserDB(0, "최오성", ""))
                            add(UserDB(1, "주오성", ""))
                            add(UserDB(2, "곽오성", ""))
                            add(UserDB(3, "김오성", ""))
                            add(UserDB(4, "박오성", ""))
                            add(UserDB(5, "이오성", ""))
                            add(UserDB(6, "신오성", ""))
                            add(UserDB(7, "안오성", ""))
                            add(UserDB(8, "유오성", ""))
                            add(UserDB(9, "소오성", ""))
                            add(UserDB(10, "한오성", ""))
                            add(UserDB(11, "위오성", ""))
                            add(UserDB(12, "용오성", ""))
                            add(UserDB(13, "정오성", ""))
                            add(UserDB(14, "황오성", ""))
                            add(UserDB(15, "지오성", ""))
                            add(UserDB(16, "예오성", ""))
                            add(UserDB(17, "강오성", ""))
                            add(UserDB(18, "유오성", ""))
                            add(UserDB(19, "고오성", ""))
                            add(UserDB(20, "허오성", ""))
                            add(UserDB(21, "윤오성", ""))
                        }

                        userDBList.forEach {
                            db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (${it.userSeq}, '${it.nickname}', '${it.profileImg}')")
                        }

                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 2)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 4)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 5)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 7)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 10)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 12)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 13)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 16)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 17)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 19)")
                        db.execSQL("insert into SubscribeDB (userSeq, userSubscribeSeq) values (0, 21)")

                        val list = arrayListOf(
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175b6688d67ff70f/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/11_175b6688d67ff70f.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175a0d4146f54e6f/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/07_175a0d4146f54e6f.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175ba32afe0395d3/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/12_175ba32afe0395d3.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175ba2578aa8ff03/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/12_175ba2578aa8ff03.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175b0b20ba8b88d3/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/10_175b0b20ba8b88d3.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/175b665c134d2b3b/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202011/11_175b665c134d2b3b.jpg"
                            ),
                            Pair(
                                "http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8",
                                "http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg"
                            ),
                        )

                        var j = 0
                        for (i in 0..21) {
                            for(k in 0..2) {
                                db.execSQL("insert into VideoDB (uploadUserSeq, title, contents, thumbnailPath, videoPath, likeCount, notLikeCount, replyCount, viewCount, uploadDate, userSeq, nickname, profileImg) values ($i, '$i 번이 올린 비디오 1', '$i 번이 올린 비디오 내용 1', '${list[j].second}', '${list[j].first}', 572, 13, 5, 123897132, ${System.currentTimeMillis()}, ${userDBList[i].userSeq}, '${userDBList[i].nickname}', '${userDBList[i].profileImg}')")

                                j++
                                if(j == list.size) j = 0
                            }
                        }

                    }
                }).build()
        }
    }
}