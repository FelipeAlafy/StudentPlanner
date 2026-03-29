package net.felipealafy.studentplanner.feature_planner.data.mapper

import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerTable

fun PlannerTable.toDomainModel(): Planner {
    return Planner(
        id = this.id,
        name = this.name,
        color = this.color,
        minimumGradeToPass = this.minimumGradeToPass,
        gradeDisplayStyle = this.gradeDisplayStyle,
    )
}

fun Planner.toDatabaseEntry(): PlannerTable {
    return PlannerTable(
        id = this.id,
        name = this.name,
        color = this.color,
        minimumGradeToPass = this.minimumGradeToPass,
        gradeDisplayStyle = this.gradeDisplayStyle,
    )
}

fun List<PlannerTable>.toDomainModel() : List<Planner> {
    return map { it.toDomainModel() }
}