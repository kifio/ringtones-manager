package kifio.ringtones

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore
import android.support.v7.app.NotificationCompat
import android.util.Log
import java.io.File
import java.util.*

object RingtonesManager {

    private val TAG = "kifio"

    fun resetRingtone(ctx: Context) {

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST)

        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val cursor = ctx.contentResolver.query(
                uri,
                projection, // The columns to return for each row
                selection, // Selection criteria
                selectionArgs, // Selection criteria
                sortOrder)  // The sort order for the returned rows

        if (!cursor.moveToFirst()) {
            Log.d(TAG, "No query results.")
        } else {
            setRandomRingtone(ctx, cursor)
        }

        cursor.close()
    }

    private fun setRandomRingtone(ctx: Context, cursor: Cursor) {

        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        var path: String?
        val title: String?
        val artist: String?
        val bound = cursor.count - 1
        val position = Random().nextInt(bound)

        do {

            path = cursor.getString(pathColumn)

            if (path.contains("/storage/emulated/0/Ringtones") && cursor.position == position) {
                title = cursor.getString(titleColumn)
                artist = cursor.getString(artistColumn)
                setRingtone(ctx, Audio(title, path, artist))
                break
            }

        } while (cursor.moveToNext())
    }


    private fun setRingtone(ctx: Context, audio: Audio) {

        val values = ContentValues()
        val file = File(audio.path)
        val cr = ctx.contentResolver

        values.put(MediaStore.MediaColumns.DATA, audio.path)
        values.put(MediaStore.MediaColumns.TITLE, audio.title)
        values.put(MediaStore.MediaColumns.SIZE, file.length())
        values.put(MediaStore.Audio.Media.ARTIST, audio.artist)
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
        values.put(MediaStore.Audio.Media.IS_ALARM, true)
        values.put(MediaStore.Audio.Media.IS_MUSIC, false)

        Thread(Runnable {  }).start()
        val uri = MediaStore.Audio.Media.getContentUriForPath(audio.path)
        cr.delete(uri,
                MediaStore.MediaColumns.DATA + "=?", arrayOf(audio.path))
        val newUri = cr.insert(uri, values)

        try {
            RingtoneManager.setActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_RINGTONE, newUri)
            showNotification(ctx, audio)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun showNotification(ctx: Context, audio: Audio) {

        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val msg = audio.artist + " - " + audio.title
        val requestCode = 0
        val intent = PendingIntent.getActivity(ctx,requestCode,
                Intent(ctx, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_audiotrack)
                .setLargeIcon(getSongImage(audio.path))
                .setContentTitle("Ringtone for today")
                .setContentText(msg)
                .setChannelId(App.CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_SMALL)
        }

        builder.setContentIntent(intent)
        manager.notify(Random(System.currentTimeMillis()).nextInt(), builder.build())
    }

    private fun getSongImage(path: String): Bitmap {

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val bytes = retriever.embeddedPicture
        val offset = 0

        return BitmapFactory.decodeByteArray(bytes, offset, bytes.size)
    }


}