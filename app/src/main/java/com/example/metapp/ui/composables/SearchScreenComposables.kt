package com.example.metapp.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.metapp.R
import com.example.metapp.domain.models.SearchResult
import com.example.metapp.ui.composables.destinations.DetailScreenDestination
import com.example.metapp.ui.viewmodels.SearchScreenContentType
import com.example.metapp.ui.viewmodels.SearchScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            val scope = rememberCoroutineScope()
            val showFAB by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }

            AnimatedVisibility(
                visible = showFAB,
                // Default animations are bugged as of now https://issuetracker.google.com/u/0/issues/224005027
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(id = R.string.general_jump_to_top)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = state.searchTerm,
                onValueChange = viewModel::onSearchTermChanged
            )

            val bottomSectionModifier = Modifier
                .fillMaxWidth()
                .weight(1f)

            AnimatedContent(
                modifier = bottomSectionModifier,
                targetState = state.contentType,
                label = "SearchScreenContent"
            ) { contentType ->
                val innerModifier = Modifier.fillMaxSize()

                when (contentType) {
                    SearchScreenContentType.LOADING -> {
                        SearchScreenLoadingIndicator(
                            modifier = innerModifier
                        )
                    }

                    SearchScreenContentType.EMPTY -> {
                        SearchScreenEmptyState(
                            modifier = innerModifier
                        )
                    }

                    SearchScreenContentType.RESULTS -> {
                        SearchScreenResults(
                            modifier = innerModifier,
                            result = state.searchResult,
                            listState = listState,
                            onItemClicked = { id ->
                                navigator.navigate(DetailScreenDestination(id = id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null
            )
        },
        label = {
            Text(text = stringResource(id = R.string.search_screen_search_placeholder))
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    onValueChange("")
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = stringResource(id = R.string.general_clear)
                )
            }
        }
    )
}

@Composable
fun SearchScreenLoadingIndicator(
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
fun SearchScreenEmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.search_screen_empty_state))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreenResults(
    result: SearchResult,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        itemsIndexed(
            items = result.ids,
            key = { _, id ->
                id
            }
        ) { index, id ->
            SearchResultItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 16.dp)
                    .minimumInteractiveComponentSize()
                    .fillMaxWidth()
                    .clickable {
                        onItemClicked(id)
                    },
                id = id
            )

            if (index != result.ids.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
fun SearchResultItem(
    id: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = id.toString()
        )
    }
}