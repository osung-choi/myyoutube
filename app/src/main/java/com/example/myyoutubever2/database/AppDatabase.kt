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
import com.example.myyoutubever2.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.concurrent.Executors

@Database(entities = [UserDB::class, SubscribeDB::class, VideoDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDAO
    abstract fun subscribeDao() : SubscribeDAO
    abstract fun videoDao() : VideoDAO

    companion object {
        private const val DB_NAME = "MyYoutubeDB"

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase = synchronized(this) {
            return INSTANCE!!
        }

        fun createDatebase(context: Context) {
            INSTANCE = buildDatabase(context)
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (0, '최오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (1, '주오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (2, '곽오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (3, '김오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (4, '박오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (5, '이오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (6, '신오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (7, '안오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (8, '유오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (9, '소오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (10, '한오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (11, '위오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (12, '용오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (13, '정오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (14, '황오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (15, '지오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (16, '예오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (17, '강오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (18, '유오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (19, '고오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (20, '허오성', '')")
                        db.execSQL("insert into UserDB (userSeq, nickname, profileImg) values (21, '윤오성', '')")

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

                        for(i in 0..21) {
                            db.execSQL("insert into VideoDB (userSeq, title, contents, thumbnailPath, videoPath, likeCount, replyCount) values ($i, '$i 번이 올린 비디오 1', '$i 번이 올린 비디오 내용 1', 'http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg', 'http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8', 100, 5)")
                            db.execSQL("insert into VideoDB (userSeq, title, contents, thumbnailPath, videoPath, likeCount, replyCount) values ($i, '$i 번이 올린 비디오 2', '$i 번이 올린 비디오 내용 2', 'http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg', 'http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8', 100, 5)")
                            db.execSQL("insert into VideoDB (userSeq, title, contents, thumbnailPath, videoPath, likeCount, replyCount) values ($i, '$i 번이 올린 비디오 3', '$i 번이 올린 비디오 내용 3', 'http://data-dev.earlybird.ai:80/image/news/base/202008/18_173ff5638ea4fe1f.jpg', 'http://cache.midibus.kinxcdn.com/hls/ch_171e807a/173ff5638ea4fe1f/playlist.m3u8', 100, 5)")
                        }

                    }
                }).build()
        }
    }
}