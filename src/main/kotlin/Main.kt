import java.util.Scanner

fun main() {
    doRun("a_an_example.in.txt")
//    doRun("b_better_start_small.in.txt")
//    doRun("f_find_great_mentors.in.txt")
}

fun doRun(example: String) {
    val scanner = Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(example)!!)

    val (contributors, projects) = parseInput(scanner)

    println(contributors)
    println(projects)

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
        val roles = mutableMapOf<String, Int>()
        for (j in 1..r) {
            roles[scanner.next()] = scanner.nextInt()
        }
        projects.add(Project(name, days, score, bestBefore, roles))
    }
    return Pair(contributors, projects)
}

class Project(val name: String, val days: Int, val score: Int, val bestBefore: Int, val roles: Map<String, Int>) {

    override fun toString(): String {
        return "Project(name='$name', days=$days, score=$score, bestBefore=$bestBefore, roles=$roles)"
    }
}

class Contributor(val name: String, val skills: MutableMap<String, Int>) {
    override fun toString(): String {
        return "Contributor(name='$name', skills=$skills)"
    }
}
