package pe.edu.upeu.appturismo202501.utils


import java.util.Calendar

fun isNight():Boolean{
    val hora= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (hora<=6 || hora>=18)
}