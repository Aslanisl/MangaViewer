package ru.mail.aslanisl.mangareader.utils.image

interface NetProgressListener {
    fun onStart()
    fun progressChanged(progress: Float)
    fun onFinish()
    fun onFailure(e: Exception)
    fun onCancel()
}