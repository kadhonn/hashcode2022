import java.io.File
import java.util.Scanner
import kotlin.math.max

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

    var projectsToDo = projects.toMutableSet()
    val availableContributors = mutableSetOf<Contributor>()
    val finishing = mutableMapOf<Int, Set<Contributor>>()
    finishing[0] = contributors.toSet()
    var day = -1
    val avgDays = projects.map { it.days }.average().toInt()

    while (projectsToDo.isNotEmpty() && finishing.isNotEmpty()) {
        println("projects to do: ${projectsToDo.size}")
        day = finishing.keys.minOrNull()!!
        availableContributors.addAll(finishing.remove(day)!!)
        projectsToDo = projectsToDo.filter { it.score(day) > 0 }.toMutableSet()

        for (project in projectsToDo.sortedBy { it.score(day+avgDays) }) {
//        for (project in projectsToDo.sortedBy { it.bestBefore }) {
            val roles = mutableListOf<Contributor>()

            roles@ for (role in project.roles) {
                val possibleContributors = mutableSetOf<Contributor>()
                for (contributor in availableContributors) {
                    if (roles.contains(contributor)) {
                        continue
                    }
                    if (contributor.skills.containsKey(role.first)) {
                        if (contributor.skills[role.first]!! >= role.second) {
                            possibleContributors.add(contributor)
                        }
                    }
                }
                if (possibleContributors.isEmpty()) {
                    break
                }
                roles.add(possibleContributors.minByOrNull { it.skills[role.first]!! }!!)
            }
            if (project.roles.size == roles.size) {
                projectsToDo.remove(project)
                assignments.add(Assignment(project.name, roles.map { it.name }))
                finishing.compute(project.days + day) { _, c -> if (c != null) c.union(roles) else roles.toSet() }
                availableContributors.removeAll(roles.toSet())
                for (i in roles.indices) {
                    val role = project.roles[i]
                    if (roles[i].skills[role.first]!! <= role.second) {
                        roles[i].skills[role.first] = roles[i].skills[role.first]!! + 1
                    }
                }
            }
        }
    }

    return assignments

}

fun doRun(example: String) {
    println("Doing example: $example")
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

    fun score(day: Int): Int {
        return max(0, score - max(0, day + days - bestBefore))
    }
}

class Contributor(val name: String, val skills: MutableMap<String, Int>) {
    override fun toString(): String {
        return "Contributor(name='$name', skills=$skills)"
    }
}
