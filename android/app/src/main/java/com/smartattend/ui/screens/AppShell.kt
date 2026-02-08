package com.smartattend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartattend.ui.theme.SmartAttendAccent
import com.smartattend.ui.theme.SmartAttendBorder
import com.smartattend.ui.theme.SmartAttendDestructive
import com.smartattend.ui.theme.SmartAttendMutedForeground
import com.smartattend.ui.theme.SmartAttendPrimary
import com.smartattend.ui.theme.SmartAttendPrimaryForeground
import com.smartattend.ui.theme.SmartAttendPrimaryGradientEnd
import com.smartattend.ui.theme.SmartAttendSuccess
import com.smartattend.ui.theme.SmartAttendWarning

private enum class AppSection(val label: String) {
    Dashboard("Dashboard"),
    Classes("Classes"),
    Students("Students"),
    Attendance("Attendance"),
    Reports("Reports"),
}

@Composable
fun AppShell() {
    val selected = remember { mutableStateOf(AppSection.Dashboard) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Sidebar(selected = selected.value, onSelect = { selected.value = it })
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            when (selected.value) {
                AppSection.Dashboard -> DashboardContent()
                AppSection.Classes -> ClassesContent()
                AppSection.Students -> StudentsContent()
                AppSection.Attendance -> AttendanceContent()
                AppSection.Reports -> ReportsContent()
            }
        }
    }
}

@Composable
private fun Sidebar(selected: AppSection, onSelect: (AppSection) -> Unit) {
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
                    Text(text = "DG", fontWeight = FontWeight.Bold, color = SmartAttendPrimary)
                }
                Column {
                    Text(text = "Diamond Ghimire", fontWeight = FontWeight.Medium)
                    Text(text = "diamondassist21@gmail.com", fontSize = 12.sp, color = SmartAttendMutedForeground)
                }
            }
            TextButton(onClick = { }) {
                Text(text = "Sign Out", color = SmartAttendMutedForeground)
            }
        }
    }
}

@Composable
private fun DashboardContent() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Column {
            Text(text = "Welcome back, Diamond!", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Here's an overview of your attendance management system.",
                color = SmartAttendMutedForeground,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            StatTile("Total Students", "4", Icons.Outlined.Group, SmartAttendPrimary)
            StatTile("Total Classes", "2", Icons.Outlined.School, SmartAttendSuccess)
            StatTile("Today's Attendance", "0%", Icons.Outlined.CheckCircle, SmartAttendDestructive)
            StatTile("Overall Attendance", "100%", Icons.Outlined.TrendingUp, SmartAttendSuccess)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Class Attendance Overview", fontWeight = FontWeight.SemiBold)
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
                                Text(text = "Tuition", fontWeight = FontWeight.Medium)
                                Text(text = "3/3 present", fontSize = 12.sp, color = SmartAttendMutedForeground)
                            }
                            StatusPill(text = "100%", color = SmartAttendSuccess)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Quick Actions", fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickActionCard("Mark Attendance", Icons.Outlined.CheckCircle, SmartAttendPrimary, Modifier.weight(1f))
                        QuickActionCard("Add Student", Icons.Outlined.Group, SmartAttendSuccess, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        QuickActionCard("Add Class", Icons.Outlined.School, SmartAttendWarning, Modifier.weight(1f))
                        QuickActionCard("View Reports", Icons.Outlined.TrendingUp, SmartAttendAccent, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassesContent() {
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
            Button(onClick = { }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add Class")
            }
        }

        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Search classes...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = SmartAttendPrimary,
                focusedLabelColor = SmartAttendPrimary,
            ),
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TableHeader(listOf("Class Name", "Description", "Students", "Created"))
                ClassRow("maths", "—", "0", "Feb 5, 2026")
                ClassRow("Tuition", "—", "4", "Feb 5, 2026")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentsContent() {
    val showAddDialog = remember { mutableStateOf(false) }
    val showUploadDialog = remember { mutableStateOf(false) }

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
                OutlinedButton(onClick = { showUploadDialog.value = true }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Bulk Upload")
                }
                Button(onClick = { showAddDialog.value = true }) {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add Student")
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Filter by class:", color = SmartAttendMutedForeground)
            Spacer(modifier = Modifier.width(12.dp))
            DropdownField(label = "All Classes")
        }

        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("Search students...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = SmartAttendPrimary,
                focusedLabelColor = SmartAttendPrimary,
            ),
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TableHeader(listOf("Roll No", "Name", "Class", "Email", "Phone"))
                StudentRow("001", "Sachin Kharel", "Tuition", "—", "—")
                StudentRow("004", "Random User", "Tuition", "—", "—")
                StudentRow("014", "Rijan Pokhrel", "Tuition", "rijan@gmail.com", "+97771417099")
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .border(1.dp, SmartAttendBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column {
                Text(text = "Success", fontWeight = FontWeight.SemiBold)
                Text(text = "Student deleted successfully", color = SmartAttendMutedForeground, fontSize = 12.sp)
            }
        }
    }

    if (showAddDialog.value) {
        StudentDialog(title = "Add New Student", onDismiss = { showAddDialog.value = false })
    }

    if (showUploadDialog.value) {
        BulkUploadDialog(onDismiss = { showUploadDialog.value = false })
    }
}

@Composable
private fun AttendanceContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(text = "Mark Attendance", style = MaterialTheme.typography.titleLarge)
            Text(text = "Select a class and date to mark attendance", color = SmartAttendMutedForeground)
        }

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
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text(text = "Class", fontWeight = FontWeight.Medium)
                        DropdownField(label = "maths")
                    }
                    Column {
                        Text(text = "Date", fontWeight = FontWeight.Medium)
                        DropdownField(label = "February 7th, 2026")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { }) {
                        Text(text = "Mark All Present")
                    }
                    OutlinedButton(onClick = { }) {
                        Text(text = "Mark All Absent")
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
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "No students in this class yet.", color = SmartAttendMutedForeground)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { }) {
                    Text(text = "Add Students")
                }
            }
        }
    }
}

@Composable
private fun ReportsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Reports", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Generate and export attendance reports.",
            color = SmartAttendMutedForeground,
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SmartAttendBorder),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(imageVector = Icons.Outlined.BarChart, contentDescription = null, tint = SmartAttendPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Reports overview coming soon", fontWeight = FontWeight.Medium)
                Text(
                    text = "Export attendance by class, student, or date.",
                    color = SmartAttendMutedForeground,
                    textAlign = TextAlign.Center,
                )
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
    modifier: Modifier = Modifier   // 👈 add this
) {
    Card(
        modifier = modifier,          // 👈 use it here
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
private fun QuickActionCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.2f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accent)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, tint = SmartAttendMutedForeground)
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = SmartAttendDestructive)
        }
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, tint = SmartAttendMutedForeground)
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = SmartAttendDestructive)
        }
    }
}

@Composable
private fun DropdownField(label: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            .border(1.dp, SmartAttendBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(text = label, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentDialog(title: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.width(520.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = title, fontWeight = FontWeight.SemiBold)
                        TextButton(onClick = onDismiss) { Text("×") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LabeledInput("Roll No", "e.g. 001", Modifier.weight(1f))
                        LabeledInput("Class", "maths", Modifier.weight(1f))
                    }
                    LabeledInput("Full Name", "Enter student name")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        LabeledInput("Email (Optional)", "student@email.com", Modifier.weight(1f))
                        LabeledInput("Phone (Optional)", "+91 XXXXX XXXXX", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        OutlinedButton(onClick = onDismiss) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(onClick = onDismiss) { Text("Add Student") }
                    }
                }
            }
        }
    }
}

@Composable
private fun BulkUploadDialog(onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.width(520.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "Bulk Upload Students", fontWeight = FontWeight.SemiBold)
                        TextButton(onClick = onDismiss) { Text("×") }
                    }
                    Text(
                        text = "Paste CSV data with the following format:",
                        color = SmartAttendMutedForeground,
                        fontSize = 12.sp,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SmartAttendAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                    ) {
                        Text(
                            text = "roll_no,full_name,class_name,email,phone\n001,John Doe,Class 10A,john@email.com,+91 12345\n002,Jane Smith,Class 10A,,",
                            fontSize = 12.sp,
                        )
                    }
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { Text("Paste CSV data here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        OutlinedButton(onClick = onDismiss) { Text("Cancel") }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = SmartAttendPrimary.copy(alpha = 0.5f)),
                        ) {
                            Text("Import")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledInput(label: String, placeholder: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text(placeholder) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = SmartAttendPrimary,
                focusedLabelColor = SmartAttendPrimary,
            ),
        )
    }
}
