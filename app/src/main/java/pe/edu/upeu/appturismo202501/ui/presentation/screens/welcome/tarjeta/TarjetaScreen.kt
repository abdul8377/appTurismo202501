package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.tarjeta

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.view.CardInputWidget

@Composable
fun TarjetaScreen(
    modifier: Modifier = Modifier,
    paymentLauncher: PaymentLauncher,
    viewModel: TarjetaViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val clientSecret = viewModel.clientSecret.collectAsState()
    val context = LocalContext.current
    val cardInputWidget = remember { CardInputWidget(context) }

    LaunchedEffect(Unit) {
        // Obtener el clientSecret del backend
        viewModel.obtenerClientSecret()
    }

    // Crear la ventana flotante (AlertDialog)
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Pagar con tarjeta") },
        text = {
            Column(modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                // Mostrar el clientSecret
                Text(text = "Client Secret:\n${clientSecret.value}")
                Spacer(modifier = Modifier.padding(8.dp))

                // Añadir CardInputWidget de Stripe
                AndroidView(
                    factory = { ctx ->
                        FrameLayout(ctx).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            addView(cardInputWidget)
                        }
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                // Lógica para procesar el pago
                val cardParams = cardInputWidget.paymentMethodCreateParams
                val currentSecret = clientSecret.value

                if (cardParams != null && currentSecret.startsWith("pi_")) {
                    val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                        paymentMethodCreateParams = cardParams,
                        clientSecret = currentSecret
                    )

                    paymentLauncher.confirm(confirmParams)

                    val paymentIntentId = currentSecret.substringBefore("_secret")
                    // TODO: Enviar el paymentIntentId al backend si es necesario

                    Toast.makeText(context, "Intentando pago...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Tarjeta o clientSecret inválido", Toast.LENGTH_LONG).show()
                }
            }) {
                Text("Pagar ahora")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
