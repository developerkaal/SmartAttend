package com.smartattend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartattend.ui.theme.SmartAttendAccent
import com.smartattend.ui.theme.SmartAttendBorder
import com.smartattend.ui.theme.SmartAttendDestructive
import com.smartattend.ui.theme.SmartAttendHeroGradientEnd
import com.smartattend.ui.theme.SmartAttendHeroGradientStart
import com.smartattend.ui.theme.SmartAttendMutedForeground
import com.smartattend.ui.theme.SmartAttendPrimary
import com.smartattend.ui.theme.SmartAttendPrimaryForeground
import com.smartattend.ui.theme.SmartAttendPrimaryGradientEnd
import com.smartattend.ui.theme.SmartAttendSuccess

@Immutable
private data class Feature(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val features = listOf(
    Feature(
        title = "Easy Attendance",
        description = "Mark attendance with just a few taps. Select class, date, and mark students as present or absent.",
        icon = Icons.Outlined.CheckCircle,
    ),
    Feature(
        title = "Student Management",
        description = "Manage student records efficiently. Add, edit, or remove students with bulk upload support.",
        icon = Icons.Outlined.SupervisedUserCircle,
    ),
    Feature(
        title = "Detailed Reports",
        description = "Generate comprehensive reports by student, class, or date. Export to CSV for further analysis.",
        icon = Icons.Outlined.TableChart,
    ),
    Feature(
        title = "Secure & Private",
        description = "Your data is protected with secure authentication and role-based access control.",
        icon = Icons.Outlined.Shield,
    ),
    Feature(
        title = "Fast & Responsive",
        description = "Works seamlessly on phones and tablets with instant updates.",
        icon = Icons.Outlined.Bolt,
    ),
    Feature(
        title = "Real-time Tracking",
        description = "Track attendance in real-time with instant statistics and visual indicators.",
        icon = Icons.Outlined.Schedule,
    ),
)

@Composable
fun HomeScreen(onNavigateToLogin: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            HeroSection(onNavigateToLogin = onNavigateToLogin)
        }
        item {
            DashboardPreview()
        }
        item {
            FeatureSection()
        }
        item {
            CallToAction(onNavigateToLogin = onNavigateToLogin)
        }
        item {
            FooterSection()
        }
    }
}

@Composable
private fun HeroSection(onNavigateToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    SmartAttendPrimary,
                                    SmartAttendPrimaryGradientEnd,
                                ),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.School,
                        contentDescription = null,
                        tint = SmartAttendPrimaryForeground,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "SmartAttend",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Button(onClick = onNavigateToLogin) {
                Text("Get Started")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SmartAttendAccent)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = null,
                    tint = SmartAttendPrimary,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Smart Attendance Management",
                    color = SmartAttendPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val title = buildAnnotatedString {
                append("Effortless Attendance ")
                withStyle(style = SpanStyle(color = SmartAttendPrimary)) {
                    append("Tracking")
                }
                append(" for ")
                withStyle(style = SpanStyle(color = SmartAttendPrimary)) {
                    append("Educators")
                }
            }
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Streamline your classroom management with our intuitive attendance system. Track, analyze, and report — all in one place.",
                textAlign = TextAlign.Center,
                color = SmartAttendMutedForeground,
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.height(46.dp),
                ) {
                    Text("Start for Free")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
                OutlinedButton(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.height(46.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
                ) {
                    Text("View Demo")
                }
            }
        }
    }
}

@Composable
private fun DashboardPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            SmartAttendAccent.copy(alpha = 0.3f),
                        ),
                    ),
                )
                .padding(20.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Dot(Color(0xFFEF4444))
                Dot(Color(0xFFF59E0B))
                Dot(Color(0xFF22C55E))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val stats = listOf(
                    "Total Students" to "248",
                    "Classes" to "12",
                    "Today's Attendance" to "94%",
                    "Overall Rate" to "87%",
                )
                stats.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        row.forEach { (label, value) ->
                            StatCard(label = label, value = value, modifier = Modifier.weight(1f))
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val statusLabels = List(12) { index ->
                if (index % 3 == 0) "Absent" else "Present"
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                statusLabels.chunked(3).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        row.forEach { label ->
                            StatusChip(
                                label = label,
                                modifier = Modifier.weight(1f),
                                isAbsent = label == "Absent",
                            )
                        }
                        if (row.size < 3) {
                            repeat(3 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SmartAttendAccent.copy(alpha = 0.3f))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Everything You Need",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Powerful features designed to make attendance management simple and efficient.",
            textAlign = TextAlign.Center,
            color = SmartAttendMutedForeground,
        )
        Spacer(modifier = Modifier.height(20.dp))

        features.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { feature ->
                    FeatureCard(feature = feature, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SmartAttendAccent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = SmartAttendPrimary,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = feature.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = feature.description,
                color = SmartAttendMutedForeground,
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CallToAction(onNavigateToLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(SmartAttendHeroGradientStart, SmartAttendHeroGradientEnd),
                ),
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ready to Get Started?",
                color = SmartAttendPrimaryForeground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Join educators who are already saving time with SmartAttend. Sign up now and start managing attendance effortlessly.",
                color = SmartAttendPrimaryForeground.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNavigateToLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SmartAttendPrimaryForeground,
                    contentColor = SmartAttendPrimary,
                ),
            ) {
                Text("Create Free Account")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(SmartAttendPrimary, SmartAttendPrimaryGradientEnd),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.School,
                    contentDescription = null,
                    tint = SmartAttendPrimaryForeground,
                    modifier = Modifier.size(16.dp),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "SmartAttend", fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "© 2025 SmartAttend. Built for educators.",
            color = SmartAttendMutedForeground,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, color = SmartAttendMutedForeground, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun StatusChip(label: String, modifier: Modifier = Modifier, isAbsent: Boolean) {
    val background = if (isAbsent) SmartAttendDestructive.copy(alpha = 0.1f) else SmartAttendSuccess.copy(alpha = 0.1f)
    val border = if (isAbsent) SmartAttendDestructive.copy(alpha = 0.2f) else SmartAttendSuccess.copy(alpha = 0.2f)
    val textColor = if (isAbsent) SmartAttendDestructive else SmartAttendSuccess
    Box(
        modifier = modifier
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .background(background, RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(color),
    )
}
