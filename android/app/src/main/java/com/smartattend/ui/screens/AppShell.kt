package com.smartattend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartattend.data.AttendanceItem
import com.smartattend.data.ClassResponse
import com.smartattend.data.StudentRequest
import com.smartattend.ui.theme.SmartAttendAccent
import com.smartattend.ui.theme.SmartAttendBorder
import com.smartattend.ui.theme.SmartAttendDestructive
import com.smartattend.ui.theme.SmartAttendMutedForeground
import com.smartattend.ui.theme.SmartAttendPrimary
import com.smartattend.ui.theme.SmartAttendPrimaryForeground
import com.smartattend.ui.theme.SmartAttendPrimaryGradientEnd
import com.smartattend.ui.theme.SmartAttendSuccess
import com.smartattend.ui.theme.SmartAttendWarning
import com.smartattend.ui.viewmodel.SmartAttendViewModel
import com.smartattend.ui.viewmodel.UiEvent
import java.time.LocalDate

private enum class AppSection(val label: String) {
    Dashboard("Dashboard"),
    Classes("Classes"),
    Students("Students"),
    Attendance("Attendance"),
    Reports("Reports"),
}

private data class StatItem(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color,
)

@Composable
fun AppShell() {
    val viewModel: SmartAttendViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val selected = remember { mutableStateOf(AppSection.Dashboard) }
    val isCompact = LocalConfiguration.current.screenWidthDp < 600
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.refreshAll()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val message = when (event) {
                is UiEvent.Success -> event.message
                is UiEvent.Error -> event.message
                is UiEvent.Offline -> event.message
            }
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            if (isCompact) {
                TopAppBar(
                    title = { Text(text = selected.value.label) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    windowInsets = WindowInsets.statusBars,
                )
            }
        },
        bottomBar = {
            if (isCompact) {
                NavigationBar {
                    AppSection.values().forEach { section ->
                        NavigationBarItem(
                            selected = selected.value == section,
                            onClick = { selected.value = section },
                            icon = {
                                Icon(
                                    imageVector = section.icon(),
                                    contentDescription = null,
                                )
                            },
                            label = { Text(text = section.label) },
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { padding ->
        if (isCompact) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                uiState.value.errorMessage?.let { message ->
                    ErrorBanner(message = message)
                }
                AppSectionContent(
                    selected = selected.value,
                    isCompact = true,
                    viewModel = viewModel,
                    uiState = uiState.value,
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
            ) {
                Sidebar(
                    selected = selected.value,
                    onSelect = { selected.value = it },
                    userName = uiState.value.userName,
                    userEmail = uiState.value.userEmail,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    uiState.value.errorMessage?.let { message ->
                        ErrorBanner(message = message)
                    }
                    AppSectionContent(
                        selected = selected.value,
                        isCompact = false,
                        viewModel = viewModel,
                        uiState = uiState.value,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppSectionContent(
    selected: AppSection,
    isCompact: Boolean,
    viewModel: SmartAttendViewModel,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
) {
    when (selected) {
        AppSection.Dashboard -> DashboardContent(isCompact, uiState)
        AppSection.Classes -> ClassesContent(isCompact, uiState, viewModel)
        AppSection.Students -> StudentsContent(isCompact, uiState, viewModel)
        AppSection.Attendance -> AttendanceContent(isCompact, uiState, viewModel)
        AppSection.Reports -> ReportsContent(isCompact, uiState, viewModel)
    }
}

private fun AppSection.icon(): ImageVector = when (this) {
    AppSection.Dashboard -> Icons.Outlined.Home
    AppSection.Classes -> Icons.Outlined.School
    AppSection.Students -> Icons.Outlined.Group
    AppSection.Attendance -> Icons.Outlined.CheckCircle
    AppSection.Reports -> Icons.Outlined.BarChart
}

@Composable
private fun Sidebar(
    selected: AppSection,
    onSelect: (AppSection) -> Unit,
    userName: String?,
    userEmail: String?,
) {
    val navItems = listOf(
        AppSection.Dashboard to Icons.Outlined.Home,
        AppSection.Classes to Icons.Outlined.School,
        AppSection.Students to Icons.Outlined.Group,
        AppSection.Attendance to Icons.Outlined.CheckCircle,
        AppSection.Reports to Icons.Outlined.BarChart,
    )

    Column(
        modifier = Modifier
            .width(240.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, SmartAttendBorder),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(SmartAttendPrimary, SmartAttendPrimaryGradientEnd),
                            ),
                            shape = RoundedCornerShape(12.dp),
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
                Text(text = "SmartAttend", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            navItems.forEach { (section, icon) ->
                val isSelected = section == selected
                val background = if (isSelected) SmartAttendPrimary else Color.Transparent
                val contentColor = if (isSelected) SmartAttendPrimaryForeground else SmartAttendMutedForeground
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(background, RoundedCornerShape(12.dp))
                        .clickable { onSelect(section) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = contentColor)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = section.label, color = contentColor, fontWeight = FontWeight.Medium)
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(SmartAttendAccent, CircleShape)
                        .border(1.dp, SmartAttendBorder, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = userName?.take(2)?.uppercase() ?: "SA",
                        fontWeight = FontWeight.Bold,
                        color = SmartAttendPrimary,
                    )
                }
                Column {
                    Text(text = userName ?: "SmartAttend User", fontWeight = FontWeight.Medium)
                    Text(
                        text = userEmail ?: "signin@smartattend.app",
                        fontSize = 12.sp,
                        color = SmartAttendMutedForeground,
                    )
                }
            }
            TextButton(onClick = { }) {
                Text(text = "Sign Out", color = SmartAttendMutedForeground)
            }
        }
    }
}

@Composable
private fun DashboardContent(
    isCompact: Boolean,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
) {
    val dashboard = uiState.dashboard
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Column {
            Text(
                text = "Welcome back${uiState.userName?.let { ", $it" } ?: ""}!",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Here's an overview of your attendance management system.",
                color = SmartAttendMutedForeground,
            )
        }

        if (dashboard == null) {
            LoadingCard()
            return
        }

        val stats = listOf(
            StatItem("Total Students", dashboard.totalStudents.toString(), Icons.Outlined.Group, SmartAttendPrimary),
            StatItem("Total Classes", dashboard.totalClasses.toString(), Icons.Outlined.School, SmartAttendSuccess),
            StatItem("Today's Attendance", "${dashboard.todayAttendance}%", Icons.Outlined.CheckCircle, SmartAttendWarning),
            StatItem("Overall Attendance", "${dashboard.overallAttendance}%", Icons.Outlined.TrendingUp, SmartAttendSuccess),
        )

        if (isCompact) {
            stats.chunked(2).forEach { rowStats ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    rowStats.forEach { stat ->
                        StatTile(
                            title = stat.title,
                            value = stat.value,
                            icon = stat.icon,
                            accent = stat.color,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                stats.forEach { stat ->
                    StatTile(stat.title, stat.value, stat.icon, stat.color, Modifier.weight(1f))
                }
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Class Attendance Overview", fontWeight = FontWeight.SemiBold)
                if (dashboard.classSummaries.isEmpty()) {
                    Text(text = "No attendance records yet.", color = SmartAttendMutedForeground)
                } else {
                    dashboard.classSummaries.forEach { summary ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SmartAttendAccent.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(text = summary.className, fontWeight = FontWeight.Medium)
                                    Text(
                                        text = "${summary.present}/${summary.total} present",
                                        fontSize = 12.sp,
                                        color = SmartAttendMutedForeground,
                                    )
                                }
                                StatusPill(text = "${summary.percentage}%", color = SmartAttendSuccess)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassesContent(
    isCompact: Boolean,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
    viewModel: SmartAttendViewModel,
) {
    val showAddDialog = remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = "Classes", style = MaterialTheme.typography.titleLarge)
                Text(text = "Manage your classes and sections", color = SmartAttendMutedForeground)
            }
            Button(onClick = { showAddDialog.value = true }) {
                Icon(imageVector = Icons.Outlined.School, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Class")
            }
        }

        if (uiState.classes.isEmpty()) {
            EmptyStateCard("No classes yet", "Create your first class to get started.")
        } else if (isCompact) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.classes.forEach { classItem ->
                    ClassCard(classItem)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TableHeader(listOf("Class Name", "Description", "Students", "Created"))
                    uiState.classes.forEach { classItem ->
                        ClassRow(
                            name = classItem.name,
                            description = classItem.description ?: "—",
                            students = classItem.studentCount.toString(),
                            created = classItem.createdAt.take(10),
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog.value) {
        AddClassDialog(
            onDismiss = { showAddDialog.value = false },
            onSubmit = { name, description ->
                viewModel.createClass(name, description) { showAddDialog.value = false }
            },
        )
    }
}

@Composable
private fun StudentsContent(
    isCompact: Boolean,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
    viewModel: SmartAttendViewModel,
) {
    val showAddDialog = remember { mutableStateOf(false) }
    val selectedClass = remember { mutableStateOf<ClassResponse?>(null) }

    LaunchedEffect(uiState.classes) {
        if (selectedClass.value == null && uiState.classes.isNotEmpty()) {
            selectedClass.value = uiState.classes.first()
        }
    }

    LaunchedEffect(selectedClass.value?.id) {
        viewModel.loadStudents(selectedClass.value?.id)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = "Students", style = MaterialTheme.typography.titleLarge)
                Text(text = "Manage student records", color = SmartAttendMutedForeground)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { showAddDialog.value = true }) {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add Student")
                }
            }
        }

        ClassDropdown(
            label = "Filter by class",
            classes = uiState.classes,
            selectedClass = selectedClass.value,
            onSelected = { selectedClass.value = it },
        )

        if (uiState.students.isEmpty()) {
            EmptyStateCard("No students yet", "Add students to start tracking attendance.")
        } else if (isCompact) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                uiState.students.forEach { student ->
                    StudentCard(student.fullName, student.rollNo, student.className, student.email, student.phone)
                }
            }
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TableHeader(listOf("Roll No", "Name", "Class", "Email", "Phone"))
                    uiState.students.forEach { student ->
                        StudentRow(
                            roll = student.rollNo,
                            name = student.fullName,
                            className = student.className,
                            email = student.email ?: "—",
                            phone = student.phone ?: "—",
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog.value) {
        AddStudentDialog(
            classes = uiState.classes,
            onDismiss = { showAddDialog.value = false },
            onSubmit = { request ->
                viewModel.createStudent(request) { showAddDialog.value = false }
            },
        )
    }
}

@Composable
private fun AttendanceContent(
    isCompact: Boolean,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
    viewModel: SmartAttendViewModel,
) {
    val selectedClass = remember { mutableStateOf<ClassResponse?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now().toString()) }
    val attendanceMap = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(uiState.classes) {
        if (selectedClass.value == null && uiState.classes.isNotEmpty()) {
            selectedClass.value = uiState.classes.first()
        }
    }

    LaunchedEffect(selectedClass.value?.id, selectedDate.value) {
        selectedClass.value?.id?.let { classId ->
            viewModel.loadStudents(classId)
            viewModel.loadAttendance(classId, selectedDate.value)
        }
    }

    LaunchedEffect(uiState.students, uiState.attendance) {
        attendanceMap.clear()
        val attendanceByStudent = uiState.attendance.associateBy { it.studentId }
        uiState.students.forEach { student ->
            attendanceMap[student.id] = attendanceByStudent[student.id]?.present ?: false
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(text = "Mark Attendance", style = MaterialTheme.typography.titleLarge)
            Text(text = "Select a class and date to mark attendance", color = SmartAttendMutedForeground)
        }

        ClassDropdown(
            label = "Class",
            classes = uiState.classes,
            selectedClass = selectedClass.value,
            onSelected = { selectedClass.value = it },
        )

        OutlinedTextField(
            value = selectedDate.value,
            onValueChange = { selectedDate.value = it },
            label = { Text("Date (YYYY-MM-DD)") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.students.isEmpty()) {
            EmptyStateCard("No students yet", "Add students to start marking attendance.")
        } else {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    uiState.students.forEach { student ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(text = student.fullName, fontWeight = FontWeight.Medium)
                                Text(text = student.rollNo, color = SmartAttendMutedForeground, fontSize = 12.sp)
                            }
                            Checkbox(
                                checked = attendanceMap[student.id] == true,
                                onCheckedChange = { checked -> attendanceMap[student.id] = checked },
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                val classId = selectedClass.value?.id ?: return@Button
                val records = attendanceMap.map { AttendanceItem(it.key, it.value) }
                viewModel.saveAttendance(classId, selectedDate.value, records) {}
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedClass.value != null && uiState.students.isNotEmpty(),
        ) {
            Text(text = "Save Attendance")
        }
    }
}

@Composable
private fun ReportsContent(
    isCompact: Boolean,
    uiState: com.smartattend.ui.viewmodel.SmartAttendUiState,
    viewModel: SmartAttendViewModel,
) {
    val selectedClass = remember { mutableStateOf<ClassResponse?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now().toString()) }

    LaunchedEffect(uiState.classes) {
        if (selectedClass.value == null && uiState.classes.isNotEmpty()) {
            selectedClass.value = uiState.classes.first()
        }
    }

    LaunchedEffect(selectedClass.value?.id) {
        viewModel.loadStudentReports(selectedClass.value?.id)
    }

    LaunchedEffect(selectedClass.value?.id, selectedDate.value) {
        selectedClass.value?.id?.let { classId ->
            viewModel.loadDateReports(classId, selectedDate.value)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Reports", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Generate and export attendance reports.",
            color = SmartAttendMutedForeground,
        )

        ClassDropdown(
            label = "Class",
            classes = uiState.classes,
            selectedClass = selectedClass.value,
            onSelected = { selectedClass.value = it },
        )

        OutlinedTextField(
            value = selectedDate.value,
            onValueChange = { selectedDate.value = it },
            label = { Text("Date (YYYY-MM-DD)") },
            leadingIcon = { Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "Student Attendance Summary", fontWeight = FontWeight.SemiBold)
                if (uiState.studentReports.isEmpty()) {
                    Text(text = "No reports available yet.", color = SmartAttendMutedForeground)
                } else if (isCompact) {
                    uiState.studentReports.forEach { report ->
                        ReportCard(
                            title = report.fullName,
                            subtitle = report.className,
                            value = "${report.percentage}%",
                        )
                    }
                } else {
                    uiState.studentReports.forEach { report ->
                        ReportRow(
                            label = report.fullName,
                            subLabel = report.className,
                            value = "${report.presentDays}/${report.totalDays}",
                            percentage = report.percentage,
                        )
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "Daily Attendance", fontWeight = FontWeight.SemiBold)
                if (uiState.dateReports.isEmpty()) {
                    Text(text = "No attendance logged for this date.", color = SmartAttendMutedForeground)
                } else {
                    uiState.dateReports.forEach { report ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(text = report.fullName, fontWeight = FontWeight.Medium)
                                Text(text = report.rollNo, color = SmartAttendMutedForeground, fontSize = 12.sp)
                            }
                            StatusPill(
                                text = if (report.present) "Present" else "Absent",
                                color = if (report.present) SmartAttendSuccess else SmartAttendDestructive,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTile(
    title: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = title, color = SmartAttendMutedForeground, fontSize = 12.sp)
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accent.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accent)
            }
        }
    }
}

@Composable
private fun StatusPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Text(text = text, color = color, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    }
}

@Composable
private fun TableHeader(columns: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SmartAttendAccent.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        columns.forEach { column ->
            Text(text = column, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ClassRow(name: String, description: String, students: String, created: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(text = description, modifier = Modifier.weight(1f), color = SmartAttendMutedForeground)
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Outlined.Group, contentDescription = null, tint = SmartAttendMutedForeground)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = students, color = SmartAttendMutedForeground)
        }
        Text(text = created, modifier = Modifier.weight(1f), color = SmartAttendMutedForeground)
    }
}

@Composable
private fun StudentRow(roll: String, name: String, className: String, email: String, phone: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = roll, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(text = name, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .weight(1f)
                .background(SmartAttendAccent, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
        ) {
            Text(text = className, fontSize = 12.sp, color = SmartAttendPrimary)
        }
        Text(text = email, modifier = Modifier.weight(1f), color = SmartAttendMutedForeground)
        Text(text = phone, modifier = Modifier.weight(1f), color = SmartAttendMutedForeground)
    }
}

@Composable
private fun ClassCard(classItem: ClassResponse) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = classItem.name, fontWeight = FontWeight.SemiBold)
            Text(text = classItem.description ?: "No description", color = SmartAttendMutedForeground)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusPill(text = "${classItem.studentCount} students", color = SmartAttendPrimary)
                StatusPill(text = classItem.createdAt.take(10), color = SmartAttendAccent)
            }
        }
    }
}

@Composable
private fun StudentCard(
    name: String,
    rollNo: String,
    className: String,
    email: String?,
    phone: String?,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = name, fontWeight = FontWeight.SemiBold)
            Text(text = "Roll No: $rollNo", color = SmartAttendMutedForeground, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusPill(text = className, color = SmartAttendPrimary)
            }
            if (!email.isNullOrBlank() || !phone.isNullOrBlank()) {
                Text(text = email ?: phone.orEmpty(), color = SmartAttendMutedForeground, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ReportCard(title: String, subtitle: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = SmartAttendMutedForeground, fontSize = 12.sp)
            }
            StatusPill(text = value, color = SmartAttendSuccess)
        }
    }
}

@Composable
private fun ReportRow(label: String, subLabel: String, value: String, percentage: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, fontWeight = FontWeight.Medium)
            Text(text = subLabel, color = SmartAttendMutedForeground, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = value, fontWeight = FontWeight.SemiBold)
            Text(text = "$percentage%", color = SmartAttendMutedForeground, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Loading data...", color = SmartAttendMutedForeground)
        }
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Composable
private fun EmptyStateCard(title: String, subtitle: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = SmartAttendMutedForeground, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ClassDropdown(
    label: String,
    classes: List<ClassResponse>,
    selectedClass: ClassResponse?,
    onSelected: (ClassResponse) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Box {
            OutlinedTextField(
                value = selectedClass?.name ?: "Select class",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.School,
                        contentDescription = null,
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = SmartAttendPrimary,
                    focusedLabelColor = SmartAttendPrimary,
                ),
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded.value = true },
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                classes.forEach { classItem ->
                    DropdownMenuItem(
                        text = { Text(classItem.name) },
                        onClick = {
                            expanded.value = false
                            onSelected(classItem)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AddClassDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String?) -> Unit,
) {
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Class") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Class Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(name.value, description.value.ifBlank { null }) },
                enabled = name.value.isNotBlank(),
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun AddStudentDialog(
    classes: List<ClassResponse>,
    onDismiss: () -> Unit,
    onSubmit: (StudentRequest) -> Unit,
) {
    val rollNo = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val selectedClass = remember { mutableStateOf<ClassResponse?>(null) }

    LaunchedEffect(classes) {
        if (selectedClass.value == null && classes.isNotEmpty()) {
            selectedClass.value = classes.first()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Student") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = rollNo.value,
                    onValueChange = { rollNo.value = it },
                    label = { Text("Roll Number") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = fullName.value,
                    onValueChange = { fullName.value = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
                ClassDropdown(
                    label = "Class",
                    classes = classes,
                    selectedClass = selectedClass.value,
                    onSelected = { selectedClass.value = it },
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = phone.value,
                    onValueChange = { phone.value = it },
                    label = { Text("Phone (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val classId = selectedClass.value?.id ?: return@Button
                    onSubmit(
                        StudentRequest(
                            rollNo = rollNo.value,
                            fullName = fullName.value,
                            classId = classId,
                            email = email.value.ifBlank { null },
                            phone = phone.value.ifBlank { null },
                        ),
                    )
                },
                enabled = rollNo.value.isNotBlank() && fullName.value.isNotBlank() && selectedClass.value != null,
            ) {
                Text("Add Student")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
