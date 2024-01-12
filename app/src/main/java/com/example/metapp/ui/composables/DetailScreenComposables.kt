package com.example.metapp.ui.composables

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.metapp.R
import com.example.metapp.domain.models.DetailData
import com.example.metapp.ui.navigation.arguments.DetailNavArgs
import com.example.metapp.ui.viewmodels.DetailScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@RootNavGraph
@Destination(navArgsDelegate = DetailNavArgs::class)
@Composable
fun DetailScreen(
    navigator: DestinationsNavigator,
    viewModel: DetailScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DetailScreenTopBar(
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = {
                    navigator.popBackStack()
                }
            )
        }
    ) {
        val isLoading by remember {
            derivedStateOf {
                state.data == null
            }
        }

        AnimatedContent(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            targetState = isLoading,
            label = "DetailScreenContent"
        ) { innerIsLoading ->
            val innerModifier = Modifier.fillMaxSize()

            if (innerIsLoading) {
                DetailScreenLoadingIndicator(modifier = innerModifier)
            } else {
                // This will always be non null
                state.data?.let { data ->
                    DetailScreenInformation(
                        modifier = innerModifier.verticalScroll(rememberScrollState()),
                        data = data
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTopBar(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.detail_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.general_navigate_back)
                )
            }
        }
    )
}

@Composable
fun DetailScreenLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DetailScreenInformation(
    data: DetailData,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp)
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        if (data.primaryImage.isNotBlank()) {
            NetworkImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                url = data.primaryImage,
                contentDescription = null
            )
        }

        if (data.additionalImages.isNotEmpty()) {
            DetailImages(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                images = data.additionalImages
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = verticalArrangement
        ) {
            DetailTextItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.detail_screen_object_title),
                text = data.title
            )

            DetailTextItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.detail_screen_object_department),
                text = data.department
            )

            val isHighlightText = when (data.isHighlight) {
                true -> {
                    stringResource(id = R.string.general_yes)
                }

                false -> {
                    stringResource(id = R.string.general_no)
                }
            }

            DetailTextItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.detail_screen_is_highlight),
                text = isHighlightText
            )

            if (data.objectUrl.isNotBlank()) {
                DetailUrlItem(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.detail_screen_website),
                    url = data.objectUrl
                )
            }
        }
    }
}

@Composable
fun DetailImages(
    images: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp)
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(
            items = images,
            key = {
                it
            }
        ) { imageUrl ->
            NetworkImage(
                modifier = Modifier.fillMaxHeight(),
                url = imageUrl,
                contentDescription = null
            )
        }
    }
}

@Composable
fun DetailItemTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    )
}

@Preview
@Composable
fun DetailTextItemPreview() {
    DetailTextItem(
        modifier = Modifier.fillMaxWidth(),
        title = "Title",
        text = "Mona Lisa"
    )
}

@Composable
fun DetailTextItem(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DetailItemTitle(title = title)

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DetailUrlItem(
    title: String,
    url: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DetailItemTitle(title = title)

        val annotatedUrl = buildAnnotatedString {
            pushUrlAnnotation(UrlAnnotation(url = url))
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(url)
            }
            pop()
        }

        val context = LocalContext.current

        ClickableText(
            text = annotatedUrl,
            onClick = { offset ->
                annotatedUrl.getUrlAnnotations(start = offset, end = offset).firstOrNull()?.let {
                    val uri = Uri.parse(it.item.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                }
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}