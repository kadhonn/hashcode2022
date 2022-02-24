import java.io.File
import java.util.Scanner

fun main() {
    doRun("a_an_example.in.txt")
    doRun("b_better_start_small.in.txt")
    doRun("c_collaboration.in.txt")
    doRun("d_dense_schedule.in.txt")
    doRun("e_exceptional_skills.in.txt")
    doRun("f_find_great_mentors.in.txt")
}

fun solve(contributors: MutableList<Contributor>, projects: MutableList<Project>): List<Assignment> {

    val assignments = mutableListOf<Assignment>()

    for (project in projects.sortedBy { it.score }.reversed()) {
        val roles = mutableListOf<String>()

        roles@ for (role in project.roles) {
            for (contributor in contributors) {
                if (roles.contains(contributor.name)) {
                    continue
                }
                if (contributor.skills.containsKey(role.first)) {
                    if (contributor.skills[role.first]!! >= role.second) {
                        roles.add(contributor.name)
                    }
                    continue@roles
                }
            }
            break
        }
        if (project.roles.size == roles.size) {
            assignments.add(Assignment(project.name, roles))
        }
    }

    return assignments

}

fun doRun(example: String) {
    println("String example: $example")
    val scanner = Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(example)!!)

    val (contributors, projects) = parseInput(scanner)

    val solution = solve(contributors, projects)

    printSolution(solution, example)
}

fun printSolution(solution: List<Assignment>, example: String) {
    val sb = StringBuilder()
    sb.appendLine(solution.size)
    for (a in solution) {
        sb.appendLine(a.project)
        for (c in a.contributors) {
            sb.append(c)
            sb.append(" ")
        }
        sb.setLength(sb.length - 1)
        sb.appendLine()
    }
    File("out\\$example").writeText(sb.toString())
}

class Assignment(val project: String, val contributors: List<String>) {
    override fun toString(): String {
        return "Assignment(project='$project', contributors=$contributors)"
    }
}

private fun parseInput(scanner: Scanner): Pair<MutableList<Contributor>, MutableList<Project>> {
    val c = scanner.nextInt()
    val p = scanner.nextInt()

    val contributors = mutableListOf<Contributor>()

    for (i in 1..c) {
        val name = scanner.next()
        val s = scanner.nextInt()
        val skills = mutableMapOf<String, Int>()
        for (j in 1..s) {
            skills[scanner.next()] = scanner.nextInt()
        }
        contributors.add(Contributor(name, skills))
    }

    val projects = mutableListOf<Project>()

    for (i in 1..p) {
        val name = scanner.next()
        val days = scanner.nextInt()
        val score = scanner.nextInt()
        val bestBefore = scanner.nextInt()
        val r = scanner.nextInt()
        val roles = mutableListOf<Pair<String, Int>>()
        for (j in 1..r) {
            roles.add(Pair(scanner.next(), scanner.nextInt()))
        }
        projects.add(Project(name, days, score, bestBefore, roles))
    }
    return Pair(contributors, projects)
}

class Project(
    val name: String,
    val days: Int,
    val score: Int,
    val bestBefore: Int,
    val roles: List<Pair<String, Int>>
) {
    override fun toString(): String {
        return "Project(name='$name', days=$days, score=$score, bestBefore=$bestBefore, roles=$roles)"
    }
}

class Contributor(val name: String, val skills: MutableMap<String, Int>) {
    override fun toString(): String {
        return "Contributor(name='$name', skills=$skills)"
    }
}
