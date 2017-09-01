package kifio.ringtones

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.NotificationCompat
import android.util.Log
import java.io.File
import java.util.*


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class SetRingtoneService : JobService() {

    private val TAG = "kifio"

    override fun onStartJob(parameters: JobParameters): Boolean {
        getRingtones()
        jobFinished(parameters, false)
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    private fun query(uri: Uri,
                      projection: Array<String>,
                      selection: String?,
                      selectionArgs: Array<String>?): Cursor {
        
        val sortOrder = null

        return contentResolver.query(
                uri,
                projection, // The columns to return for each row
                selection, // Selection criteria
                selectionArgs, // Selection criteria
                sortOrder)  // The sort order for the returned rows
    }

    private fun getRingtones(): ArrayList<Audio> {

        var audios = ArrayList<Audio>()

        val cursor = query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID), null, null)

        if (!cursor.moveToFirst()) {
            Log.d(TAG, "No query results.")
        } else {
            audios = getRingtonesFromCursor(cursor)
        }

        cursor.close()
        return audios
    }

    private fun getRingtonesFromCursor(cursor: Cursor): ArrayList<Audio> {

        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

        var audio: Audio? = null
        val audios = ArrayList<Audio>()
        val albumsIds = HashSet<String>()

        do {

            audio = Audio(
                    cursor.getString(titleColumn),
                    cursor.getString(pathColumn),
                    cursor.getString(artistColumn),
                    cursor.getString(albumColumn),
                    null
            )

            albumsIds.add(audio.albumID)
            audios.add(audio)

            Log.d(TAG, audio.toString())

        } while (cursor.moveToNext())

        setAlbumsToAudios(getAlbums(albumsIds), audios)
        return audios
    }

    private fun setAlbumsToAudios(albums: HashMap<String, SetRingtoneService.Album>, audios: ArrayList<Audio>) {

        for(key in albums.keys) {
            for (audio in audios) {
                audio.album = albums[key]
            }
        }

        setRingtone(audios[Random().nextInt(audios.size - 1)])
    }

    private fun setRingtone(audio: SetRingtoneService.Audio) {

        showNotification(audio)


//        val values = ContentValues()
//        val file = File(audio.path)
//        values.put(MediaStore.MediaColumns.DATA, audio.path)
//        values.put(MediaStore.MediaColumns.TITLE, "ring")
//        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
//        values.put(MediaStore.MediaColumns.SIZE, file.length())
//        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name)
//        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
//        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true)
//        values.put(MediaStore.Audio.Media.IS_ALARM, true)
//        values.put(MediaStore.Audio.Media.IS_MUSIC, false)
//
//        val uri = MediaStore.Audio.Media.getContentUriForPath(audio.path)
//        val newUri = contentResolver.insert(uri, values)
//
//        try {
//            RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri)
//        } catch (t: Throwable) {
//            t.printStackTrace()
//        }

    }

    private fun getAlbums(albums: Set<String>): HashMap<String, Album> {

        val albumsMap = HashMap<String, Album>()
        val selection = MediaStore.Audio.Albums._ID + " IN (?, ?, ?)"
        val selectionArgs = albums.toTypedArray()
        
        val cursor = query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Albums.ALBUM_ART,
                    MediaStore.Audio.Albums.ALBUM), selection, selectionArgs)

        if (!cursor.moveToFirst()) {
            Log.d(TAG, "No query results.")
        } else {

            val albumArtColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val albumIDColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)

            var album: Album? = null

            do {

                album = Album(
                        cursor.getString(albumColumn),
                        cursor.getString(albumArtColumn)
                )

                albumsMap.put(cursor.getString(albumIDColumn), album)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return albumsMap

    }

    private fun showNotification(audio: Audio) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val msg = audio.artist + " - " + audio.title
        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_audiotrack)
                .setContentTitle("Ringtone for today")
                .setContentText(msg)
                .setChannelId(App.CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_SMALL)
        }

        val intent = PendingIntent.getActivity(this, 0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(intent)
        manager.notify(Random(System.currentTimeMillis()).nextInt(), builder.build())
    }


    data class Audio(val title: String, val path: String, val artist: String?, val albumID: String, var album: Album?)
    data class Album(val title: String, val albumArt: String)
}
