<H1>Assumptions:</H1>
<li>Course is unique per group, so one course is taught only once; </li>
<li>Each course is taught twice per week - one lecture and one practical lesson;</li>
<li>If teacher can teach a course, he can teach both practical and lecture lessons;</li>
<li>Lecture can happen only in big rooms, practical lessons can happen both in big and small rooms;</li>

To run: 
Simply run MainApp in Genetic.scala

Example of a schedule: 
Monday:
Pair 1: Lesson(Course(10 CV), Teacher(5, F, true), Practical, Room(5, PracticalRoom), Group(2, true))
Lesson(Course(30 Differential inequalities), Teacher(13, U, true), Lecture, Room(0, LectureRoom), Group(5, true))
Lesson(Course(23 Parallel computations), Teacher(12, Y, true), Practical, Room(1, PracticalRoom), Group(1, true))
Lesson(Course(20 Modern technologies), Teacher(11, T, true), Lecture, Room(2, LectureRoom), Group(4, true))
Pair 2: Lesson(Course(23 Parallel computations), Teacher(12, Y, true), Lecture, Room(2, LectureRoom), Group(1, true))
Lesson(Course(5 OOP), Teacher(4, E, true), Practical, Room(1, PracticalRoom), Group(2, true))
Lesson(Course(25 Parallel cloud computations), Teacher(13, U, true), Practical, Room(3, PracticalRoom), Group(4, true))
Lesson(Course(2 Andan), Teacher(1, B, true), Lecture, Room(4, LectureRoom), Group(3, false))
Lesson(Course(19 Telecom), Teacher(10, R, true), Lecture, Room(0, LectureRoom), Group(6, true))
Pair 3: Lesson(Course(25 Parallel cloud computations), Teacher(13, U, true), Lecture, Room(2, LectureRoom), Group(4, true))
Lesson(Course(10 CV), Teacher(5, F, true), Lecture, Room(0, LectureRoom), Group(2, true))
Lesson(Course(13 Algorithms), Teacher(7, H, true), Lecture, Room(4, LectureRoom), Group(3, true))


Tuesday:
Pair 1: Lesson(Course(12 Numerical methods), Teacher(6, G, true), Practical, Room(5, PracticalRoom), Group(0, true))
Lesson(Course(5 OOP), Teacher(4, E, true), Lecture, Room(0, LectureRoom), Group(2, true))
Lesson(Course(15 OS), Teacher(9, W, true), Practical, Room(2, LectureRoom), Group(3, true))
Pair 2: Lesson(Course(4 Programming), Teacher(2, C, true), Practical, Room(3, PracticalRoom), Group(2, true))
Lesson(Course(9 NLP), Teacher(5, F, true), Lecture, Room(4, LectureRoom), Group(5, true))
Lesson(Course(22 Genetic algorithms), Teacher(11, T, true), Lecture, Room(0, LectureRoom), Group(4, true))
Lesson(Course(0 How to learn), Teacher(1, B, true), Lecture, Room(2, LectureRoom), Group(0, true))
Pair 3: Lesson(Course(9 NLP), Teacher(5, F, true), Practical, Room(1, PracticalRoom), Group(5, true))
Lesson(Course(19 Telecom), Teacher(10, R, true), Practical, Room(3, PracticalRoom), Group(6, true))
Lesson(Course(3 Intellectual Systems), Teacher(0, A, true), Lecture, Room(4, LectureRoom), Group(0, true))
Lesson(Course(24 Cloud computations), Teacher(14, I, true), Lecture, Room(0, LectureRoom), Group(4, true))


Wednesday:
Pair 1: Lesson(Course(24 Cloud computations), Teacher(14, I, true), Practical, Room(3, PracticalRoom), Group(4, true))
Lesson(Course(13 Algorithms), Teacher(5, F, true), Practical, Room(5, PracticalRoom), Group(3, true))
Lesson(Course(21 Hard maths), Teacher(11, T, true), Lecture, Room(4, LectureRoom), Group(1, true))
Lesson(Course(7 Philosophy), Teacher(3, D, true), Practical, Room(0, LectureRoom), Group(2, true))
Pair 2: Lesson(Course(6 FP), Teacher(3, D, true), Practical, Room(0, LectureRoom), Group(2, true))
Lesson(Course(20 Modern technologies), Teacher(11, T, true), Practical, Room(5, PracticalRoom), Group(4, true))
Lesson(Course(12 Numerical methods), Teacher(6, G, true), Lecture, Room(4, LectureRoom), Group(0, true))
Lesson(Course(2 Andan), Teacher(1, B, true), Lecture, Room(2, LectureRoom), Group(1, true))
Lesson(Course(17 Statistics), Teacher(9, W, true), Practical, Room(3, PracticalRoom), Group(6, true))
Pair 3: Lesson(Course(17 Statistics), Teacher(9, W, true), Lecture, Room(4, LectureRoom), Group(6, true))
Lesson(Course(4 Programming), Teacher(2, C, true), Lecture, Room(2, LectureRoom), Group(2, true))
Lesson(Course(29 Differential equalities), Teacher(12, Y, true), Practical, Room(3, PracticalRoom), Group(5, true))
Lesson(Course(2 Andan), Teacher(1, B, true), Practical, Room(0, LectureRoom), Group(1, true))


Thursday:
Pair 1: Lesson(Course(15 OS), Teacher(9, W, true), Lecture, Room(2, LectureRoom), Group(3, true))
Lesson(Course(30 Differential inequalities), Teacher(13, U, true), Practical, Room(4, LectureRoom), Group(5, true))
Lesson(Course(0 How to learn), Teacher(1, B, true), Practical, Room(1, PracticalRoom), Group(0, true))
Pair 2: Lesson(Course(8 AI), Teacher(4, E, true), Lecture, Room(4, LectureRoom), Group(0, true))
Lesson(Course(18 Computational geometry), Teacher(10, R, true), Practical, Room(3, PracticalRoom), Group(6, true))
Lesson(Course(11 ML), Teacher(5, F, true), Practical, Room(2, LectureRoom), Group(2, true))
Lesson(Course(16 Psychology), Teacher(8, Q, true), Practical, Room(5, PracticalRoom), Group(3, true))
Pair 3: Lesson(Course(18 Computational geometry), Teacher(10, R, true), Lecture, Room(4, LectureRoom), Group(6, true))
Lesson(Course(7 Philosophy), Teacher(3, D, true), Lecture, Room(2, LectureRoom), Group(2, true))
Lesson(Course(21 Hard maths), Teacher(11, T, true), Practical, Room(0, LectureRoom), Group(1, true))
Lesson(Course(3 Intellectual Systems), Teacher(0, A, true), Practical, Room(1, PracticalRoom), Group(0, true))


Friday:
Pair 1: Lesson(Course(8 AI), Teacher(4, E, true), Practical, Room(0, LectureRoom), Group(0, true))
Lesson(Course(16 Psychology), Teacher(8, Q, true), Lecture, Room(2, LectureRoom), Group(3, true))
Lesson(Course(28 Algebra), Teacher(14, I, true), Lecture, Room(4, LectureRoom), Group(5, true))
Pair 2: Lesson(Course(22 Genetic algorithms), Teacher(11, T, true), Practical, Room(3, PracticalRoom), Group(4, true))
Lesson(Course(1 Matan), Teacher(0, A, true), Practical, Room(5, PracticalRoom), Group(0, true))
Lesson(Course(29 Differential equalities), Teacher(12, Y, true), Lecture, Room(2, LectureRoom), Group(5, true))
Lesson(Course(14 Web programming), Teacher(6, G, true), Practical, Room(4, LectureRoom), Group(3, true))
Pair 3: Lesson(Course(1 Matan), Teacher(0, A, true), Lecture, Room(4, LectureRoom), Group(0, true))
Lesson(Course(11 ML), Teacher(5, F, true), Lecture, Room(0, LectureRoom), Group(2, true))
Lesson(Course(28 Algebra), Teacher(14, I, true), Practical, Room(1, PracticalRoom), Group(5, true))
Lesson(Course(14 Web programming), Teacher(6, G, true), Lecture, Room(2, LectureRoom), Group(3, true))