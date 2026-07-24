package com.algorithmicsluque.miremotito.ui.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

val RemoteSurfaceShape = RoundedCornerShape(60.dp)
val FullRoundedShape = RoundedCornerShape(100.dp)
val DPadShape = RoundedCornerShape(1000.dp)

// Expressive Segmented List Shapes
val TopSegmentShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
val MiddleSegmentShape = RoundedCornerShape(4.dp)
val BottomSegmentShape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 28.dp, bottomEnd = 28.dp)
val FullSegmentShape = RoundedCornerShape(28.dp)

interface CornerSize {
    val topStart: Float
    val topEnd: Float
    val bottomEnd: Float
    val bottomStart: Float
}

// 12-Sided-CookieShape con bordes redondeados (Dodecágono festoneado suave)
val CookieShape = GenericShape { size, _ ->
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radius = size.width / 2f
    val innerRadius = radius * 0.90f
    val sides = 12
    val angleStep = (2 * Math.PI / sides).toFloat()

    // Guardamos los puntos de la estrella/galleta para procesarlos con curvas
    val pointsX = FloatArray(sides * 2)
    val pointsY = FloatArray(sides * 2)

    for (i in 0 until sides) {
        val outerAngle = i * angleStep
        val innerAngle = outerAngle + angleStep / 2f

        // Vértice exterior (Punta)
        pointsX[i * 2] = centerX + radius * cos(outerAngle - Math.PI.toFloat() / 2f)
        pointsY[i * 2] = centerY + radius * sin(outerAngle - Math.PI.toFloat() / 2f)

        // Vértice interior (Valle)
        pointsX[i * 2 + 1] = centerX + innerRadius * cos(innerAngle - Math.PI.toFloat() / 2f)
        pointsY[i * 2 + 1] = centerY + innerRadius * sin(innerAngle - Math.PI.toFloat() / 2f)
    }

    // Punto de partida: el punto medio entre el último valle y la primera punta
    val totalPoints = sides * 2
    val startX = (pointsX[totalPoints - 1] + pointsX[0]) / 2f
    val startY = (pointsY[totalPoints - 1] + pointsY[0]) / 2f
    moveTo(startX, startY)

    // Conectamos usando curvas de Bézier cuadráticas pasadas por los puntos medios
    for (i in 0 until totalPoints) {
        val nextIndex = (i + 1) % totalPoints
        // El punto medio hacia donde va la curva
        val midX = (pointsX[i] + pointsX[nextIndex]) / 2f
        val midY = (pointsY[i] + pointsY[nextIndex]) / 2f

        // pointsX[i] actúa como la "esquina invisible" que atrae y redondea la curva
        quadraticTo(pointsX[i], pointsY[i], midX, midY)
    }
    close()
}

// FlowerShape con bordes redondeados (9 Pétalos suaves)
val FlowerShape = GenericShape { size, _ ->
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radius = size.width / 2f
    val innerRadius = radius * 0.70f // Bajamos un poco a 0.85 para marcar más los pétalos
    val sides = 9
    val angleStep = (2 * Math.PI / sides).toFloat()

    val pointsX = FloatArray(sides * 2)
    val pointsY = FloatArray(sides * 2)

    for (i in 0 until sides) {
        val outerAngle = i * angleStep
        val innerAngle = outerAngle + angleStep / 2f

        pointsX[i * 2] = centerX + radius * cos(outerAngle - Math.PI.toFloat() / 2f)
        pointsY[i * 2] = centerY + radius * sin(outerAngle - Math.PI.toFloat() / 2f)

        pointsX[i * 2 + 1] = centerX + innerRadius * cos(innerAngle - Math.PI.toFloat() / 2f)
        pointsY[i * 2 + 1] = centerY + innerRadius * sin(innerAngle - Math.PI.toFloat() / 2f)
    }

    val totalPoints = sides * 2
    val startX = (pointsX[totalPoints - 1] + pointsX[0]) / 2f
    val startY = (pointsY[totalPoints - 1] + pointsY[0]) / 2f
    moveTo(startX, startY)

    for (i in 0 until totalPoints) {
        val nextIndex = (i + 1) % totalPoints
        val midX = (pointsX[i] + pointsX[nextIndex]) / 2f
        val midY = (pointsY[i] + pointsY[nextIndex]) / 2f

        quadraticTo(pointsX[i], pointsY[i], midX, midY)
    }
    close()
}
