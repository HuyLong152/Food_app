@file:OptIn(ExperimentalPagerApi::class)

package com.example.food_app.view.Main

import android.R.string
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_app.R
import com.example.food_app.data.model.LoaderIntro
import com.example.food_app.data.model.OnBoardingData
import com.example.food_app.ui.theme.Food_appTheme
//import com.example.food_app.ui.theme.MainColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    openLoginAction:() -> Unit
) {
    stringResource(id = R.string.newValue)
    val items = ArrayList<OnBoardingData>()
    items.add(
        OnBoardingData(
            R.raw.animation2,
            stringResource(id = R.string.title1),
             stringResource(id = R.string.des1)
        )
    )
    items.add(
        OnBoardingData(
            R.raw.animation3,
             stringResource(id = R.string.title2),
            stringResource(id = R.string.des2)
        )
    )
    items.add(
        OnBoardingData(
            R.raw.onboarding_anmation1,
             stringResource(id = R.string.title3),
            stringResource(id = R.string.des3)
        )
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(
        pageCount = items.size,
//        initialOffscreenLimit = 2,
        infiniteLoop = true,
        initialPage = 0
    )
    OnBoardingPager(
        item = items,
        pagerState = pagerState,
        modifier = Modifier
            .fillMaxWidth(),
        openLoginAction
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingPager(
    item: List<OnBoardingData>,
    pagerState: com.google.accompanist.pager.PagerState,
    modifier: Modifier = Modifier,
    openLoginAction:() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier
                        .padding(60.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoaderIntro(
                        modifier = Modifier
                            .size(300.dp)
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterHorizontally), item[page].image
                    )
                    Text(
                        text = item[page].title,
                        modifier = Modifier.padding(top = 50.dp),
                        color = Food_appTheme.myColors.textBlackColor,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = item[page].desc,
                        modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                        color = Food_appTheme.myColors.textBlackColor,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            PagerIndicator(item.size, pagerState.currentPage)
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomSection(pagerState.currentPage,pagerState,openLoginAction)
        }
    }
}

@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 60.dp)
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }
    }

}

@Composable
fun Indicator(isSelected: Boolean) {
    val with = animateDpAsState(targetValue = if (isSelected) 25.dp else 10.dp)
    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(10.dp)
            .width(with.value)
            .clip(CircleShape)
            .background(
                if (isSelected) Color.Red else Color.Gray.copy(0.5f)
            )
    )
}

//@Composable
//fun BottomSection(currentPager: Int,openLoginAction: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .padding(bottom = 20.dp)
//            .fillMaxWidth(),
//        horizontalArrangement = if (currentPager != 2) Arrangement.SpaceBetween else Arrangement.SpaceEvenly
//    ) {
//        if (currentPager == 2) {
//            Button(
//                onClick = { openLoginAction()},
//                shape = RoundedCornerShape(50),
//                colors = ButtonDefaults.buttonColors(Food_appTheme.myColors.mainColor)
//            ) {
//                Text(
//                    text = "Get Started",
//                    modifier = Modifier
//                        .padding(vertical = 8.dp, horizontal = 40.dp),
//                    color = Color.White,
//                )
//            }
//        } else {
//            SkipNextButton(text = "Skip", modifier = Modifier.padding(start = 20.dp))
//            SkipNextButton(text = "Next", modifier = Modifier.padding(end = 20.dp))
//        }
//    }
//}


@Composable
fun BottomSection(
    currentPager: Int,
    pagerState: PagerState,
    openLoginAction: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (currentPager != 2) Arrangement.SpaceBetween else Arrangement.SpaceEvenly
    ) {
        if (currentPager == 2) {
            Button(
                onClick = { openLoginAction() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(Food_appTheme.myColors.mainColor)
            ) {
                Text(
                    text =  stringResource(id = R.string.getStart),
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 40.dp),
                    color = Color.White,
                )
            }
        } else {
            SkipNextButton(
                text = stringResource(id = R.string.skip),
                modifier = Modifier.padding(start = 20.dp),
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(currentPager + 1)
                    }
                } // Di chuyển đến trang tiếp theo
            )
            SkipNextButton(
                text = stringResource(id = R.string.next),
                modifier = Modifier.padding(end = 20.dp),
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(currentPager + 1)
                    }
                } // Di chuyển đến trang tiếp theo
            )
        }
    }
}

@Composable
fun SkipNextButton(text: String, modifier: Modifier,onClick :() -> Unit) {
    Text(
        text = text,
        color = Food_appTheme.myColors.textBlackColor,
        modifier = modifier
            .clickable { onClick() },
        fontSize = 18.sp,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium
    )
}





