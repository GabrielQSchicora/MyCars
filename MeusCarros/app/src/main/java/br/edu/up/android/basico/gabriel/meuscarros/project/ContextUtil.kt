package br.edu.up.android.basico.gabriel.meuscarros.project

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.os.Vibrator
import android.support.v4.app.NotificationCompat


//ID da Notificação
    var mNotificationId = 1

    //Função que cria notificação
    fun createNotify(context: Context, intent: Intent, title: String, text: String, icon: Int){

        //Cria uma identificação do canal
        val channelId = "br.edu.up.android.basico.gabriel.meuscarros.project"

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        //Pega o serviço da notificação
        val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(mNotificationId.toString(), title, importance)
            channel.description = text
            channel.enableLights(true)
            channel.lightColor = Color.BLUE

            nManager?.createNotificationChannel(channel)

            Notification.Builder(context, channelId)
        }else{
            Notification.Builder(context)
        }.apply{
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(icon)
        }.build()

        //Cria a notificação
        nManager.notify(mNotificationId, mNotification as Notification)

        //Acrescenta um para o ID da proxima notificação
        mNotificationId += 1

        createVibration(context, 150)
    }

    fun createVibration(context: Context, time: Int){
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val teste = longArrayOf(0L, time.toLong(), 200L, time.toLong())
        vibrator!!.vibrate(teste, -1)
    }