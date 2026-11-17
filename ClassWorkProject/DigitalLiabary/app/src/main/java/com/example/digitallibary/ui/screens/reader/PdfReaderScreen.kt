package com.example.digitallibary.ui.screens.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitallibary.ui.theme.NavyDark
import com.example.digitallibary.ui.theme.PurpleAccent
import com.example.digitallibary.viewmodel.PdfReaderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfReaderScreen(
    bookId: Long,
    readerViewModel: PdfReaderViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        readerViewModel.loadPdf(context, bookId)
    }

    val book by readerViewModel.book.collectAsState()
    val currentPage by readerViewModel.currentPage.collectAsState()
    val totalPages by readerViewModel.totalPages.collectAsState()
    val pageBitmap by readerViewModel.pageBitmap.collectAsState()
    val zoomScale by readerViewModel.zoomScale.collectAsState()
    val isLoading by readerViewModel.isLoading.collectAsState()

    var scale by remember { mutableFloatStateOf(1f) }

    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        scale = (scale * zoomChange).coerceIn(0.8f, 3.0f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book?.pdfFileName ?: "Document.pdf",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search in PDF")
                    }
                    IconButton(onClick = { readerViewModel.resetZoom(); scale = 1.0f }) {
                        Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Reset Zoom")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyDark,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 12.dp,
                color = NavyDark
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Page Navigation Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { readerViewModel.prevPage() },
                            enabled = currentPage > 1
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Previous Page",
                                tint = if (currentPage > 1) Color.White else Color.Gray
                            )
                        }

                        Text(
                            text = "$currentPage / $totalPages",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = { readerViewModel.nextPage() },
                            enabled = currentPage < totalPages
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Next Page",
                                tint = if (currentPage < totalPages) Color.White else Color.Gray
                            )
                        }
                    }

                    // Page Progress Slider & Zoom Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { readerViewModel.zoomOut(); scale = (scale - 0.2f).coerceAtLeast(0.8f) }) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.White)
                        }

                        Slider(
                            value = currentPage.toFloat(),
                            onValueChange = { readerViewModel.jumpToPage(it.toInt()) },
                            valueRange = 1f..totalPages.toFloat().coerceAtLeast(1f),
                            steps = if (totalPages > 2) totalPages - 2 else 0,
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = PurpleAccent,
                                activeTrackColor = PurpleAccent,
                                inactiveTrackColor = Color.Gray.copy(alpha = 0.4f)
                            )
                        )

                        IconButton(onClick = { readerViewModel.zoomIn(); scale = (scale + 0.2f).coerceAtMost(3.0f) }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", tint = Color.White)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = PurpleAccent)
            } else if (pageBitmap != null) {
                Image(
                    bitmap = pageBitmap!!.asImageBitmap(),
                    contentDescription = "PDF Page $currentPage",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale * zoomScale,
                            scaleY = scale * zoomScale
                        )
                        .transformable(state = transformableState)
                        .padding(12.dp)
                )
            } else {
                Text(
                    text = "Unable to render PDF page.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
