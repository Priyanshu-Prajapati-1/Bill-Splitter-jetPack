package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettip.component.InputField
import com.example.jettip.ui.theme.JetTipTheme
import com.example.jettip.util.calculateTotalPerPerson
import com.example.jettip.util.calculateTotalTip
import com.example.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTipTheme {
                MyApp {
//                    TopHeader()
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        content()
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 0.0, totalAmount: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 5.dp)
            .height(180.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(15.dp))) // same as below
//            .clip(shape = RoundedCornerShape(15.dp))
        ,
        color = Color(0xFFE9D7F7),
        border = BorderStroke(1.dp, Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Total Per Person",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "$$total",
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .width(250.dp)
                    .background(Color.Black)
            )

            Text(text = "Total Amount - $$totalAmount",
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W500
                )
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    BillForm() { billAmount ->
        Log.d("amt", "MainContent : $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {}
) {
    val totalPerPersonState = remember { mutableDoubleStateOf(0.0) }

    val totalBillState = remember { mutableStateOf("") }

    val validState = remember(totalBillState.value) { totalBillState.value.trim().isNotEmpty() }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember { mutableFloatStateOf(0f) }

    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()

    val splitByState = remember { mutableIntStateOf(1) }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember { mutableDoubleStateOf(0.0) }

    val totalAmount = remember { mutableDoubleStateOf(0.0) }

    Column {
        TopHeader(totalPerPerson = totalPerPersonState.value, totalAmount = totalAmount.doubleValue)
        Surface(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
//             TopHeader()
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        // TODO- onValueChanged
                        keyboardController?.hide()
                    }
                )
//            if (validState) {
//                Text(text = " Valid")
                Row(
                    modifier = Modifier
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier
                            .padding(start = 25.dp)
                            .align(
                                alignment = Alignment.CenterVertically
                            ),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.intValue =
                                    if (splitByState.intValue > 1) splitByState.intValue - 1 else 1

                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.intValue,
                                    tipPercentage = tipPercentage
                                )
                                totalAmount.doubleValue = tipAmountState.doubleValue + totalBillState.value.toDouble()
                            })
                        Text(
                            text = "${splitByState.intValue}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 12.dp),
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if (splitByState.intValue < range.last) {
                                    splitByState.intValue = splitByState.intValue + 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.intValue,
                                    tipPercentage = tipPercentage
                                )
                                totalAmount.doubleValue = tipAmountState.doubleValue + totalBillState.value.toDouble()
                            })
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 15.dp)
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.width(150.dp))
                    Text(
                        text = "$${tipAmountState.doubleValue}",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$tipPercentage%",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // slider
                    Slider(
                        value = sliderPositionState.floatValue,
                        onValueChange = { newValue ->
                            sliderPositionState.floatValue = newValue
                            tipAmountState.doubleValue = calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage
                                )
                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.intValue,
                                tipPercentage = tipPercentage,
                            )
                            totalAmount.doubleValue = tipAmountState.doubleValue + totalBillState.value.toDouble()
                        },
                        modifier = Modifier
                            .padding(horizontal = 15.dp),
//                        steps = 5,
                        onValueChangeFinished = {

                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Black,
                            activeTrackColor = Color.DarkGray,
                            inactiveTrackColor = Color.LightGray,
                        )
                    )
                }
//            } else {
//                Box {
//                }
//            }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTipTheme {
        MyApp {
            TopHeader()
        }
    }
}