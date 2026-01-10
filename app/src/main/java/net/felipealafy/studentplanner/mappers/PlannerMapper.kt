package net.felipealafy.studentplanner.mappers

import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.tablemodels.PlannerTable

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