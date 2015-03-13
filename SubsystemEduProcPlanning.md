# Подсистема планирования учебного процесса #

1. Подсистема планирования учебного процесса включает (UseCase):
  * ведение описательных записей для учебных планов по направлениям и специальностям (связанных с соответствующей лицензионной записью [Описание подсистемы ведения лицензий](SubsystemLicensing.md));
  * разработку общих для всех форм обучения учебных планов (EduPlan);
  * разработку на основе общего плана учебных планов по формам обучения;
  * разработку на основе учебных планов по формам обучения рабочих учебных планов, привязанных к году поступления обучающихся по ним студентов (поскольку рабочие планы привязываются к каждому студенту, то они одновременно являются индивидуальными планами обучения студента);
  * разработку на основе рабочих учебных планов нагрузки преподавателей.
Подсистема ориентирована на максимальную (в рамках нормативных документов) реализацию выбора студентом дисциплин.

2. Основными свойствами описательных записей учебного плана являются:
  * Данные лицензионной записи: шифр, область знаний, направление подготовки (с вариантами подготовки, если есть), специальность (с вариантами подготовки, если есть), область действия лицензии, образовательно-квалификационный уровень;
  * квалификация (Відповідно до НАЦІОНАЛЬНИЙ КЛАСИФІКАТОР УКРАЇНИ КЛАСИФІКАТОР ПРОФЕСІЙ ДК 003:2005 или более новый);
  * Даты: ввода плана в действие, утверждения;
  * Наименование утвердившей организации: руководство КПУ, комиссия МОН и др.;
  * Статус учебного плана: действующий, разрабатываемый, выведенный из действия.

3. При добавлении/редактировании описательной записи учебного плана (имеет право роль EduProcPlanningManager):
  * создаётся объект EduPlan со статусом Разрабатываемый (при добавлении);
  * устанавливается его свойство Лицензионная запись;
  * указывается квалификация завершивших обучение по этому плану;
  * указывается предположительная Дата ввода в действие (дата начала обучения по данному учебному плану).

4. После добавления описательной записи учебного плана заполняется Общий учебный план (EduPlan) для всех форм обучения:
  * создаются объекты циклов учебного плана (EduPlanCycle) в соответствии с требованиями Освітньо-Професійної Програми (ОПП);
  * к циклам учебного плана добавляются объекты дисциплин учебного плана (EduPlanDiscipline), по сути являющимися метадисциплинами, которые позже будут определены реальными дисциплинами (объектами Discipline);
  * общий учебный план утверждается с указанием даты утверждения и утвердивших организаций, после этого он меняет статус на Действующий и удаляются элементы управления Редактировать и Удалить учебный план в списке учебных планов и элементы управления Добавить, Редактировать и Удалить цикл учебного плана и Добавить, Редактировать и Удалить дисциплину учебного плана для записей Общего учебного плана;
  * общий учебный план может быть выведен из действия с указанием даты выведения из действия и выполнвиших это организаций, после чего он меняет статус на Выведенный из действия.

5. При добавлении/редактировании цикла учебного плана (EduPlanCycle) вводится/редактируется следующая информация:
  * номер цикла дисциплины в соответствии с ОПП;
  * название цикла дисциплин в соответствии с ОПП (выбирается из справочника);
  * общее количество ECTS-кредитов цикла в соответствии с ОПП и другими нормативами (используется при добавлении в цикл дисциплин для контроля суммарного количества ECTS-кредитов дисциплин цикла).

6. При добавлении/редактировании дисциплины цикла учебного плана (EduPlanDiscipline) вводится/редактируется следующая информация:
  * номер дисциплины (рекомендуется для обязательных дисциплин проставлять номер в соответствии с ОПП), для выборочных дисциплин рекомендуется суффикс в, для практик - суффикс п, для дипломных работ и экзаменов - суффикс д;
  * название дисциплины (по сути она является метадисциплиной (см. п.4)) - для выборочных дисциплин рекомендуется название аббревитатура названия цикла-№ п/п;
  * общее число ECTS-кредитов дисциплины, при этом в заголовочной части окна указывается сколько кредитов УЖЕ выделено на дисциплины данного цикла и общее количесвто кредитов цикла, например, 16 кредитів з 24;
  * тип дисциплины (обязательная или выборочная);
  * количество экзаменов, зачётов и курсовых работ по дисциплине;
  * указываются формы обучения, изучающие данную дисциплину;
  * указывается инициатор ввода дисциплины в план (ОПП. приказ МОН, выбор ВНЗ, выбор студента и др.)

7. После завершения заполнения общего учебного плана осуществляется переход к заполнению учебных планов по дневной и заочной формам обучения. При этом из общего учебного плана автоматически переносятся значения:
  * номер и название циклов и их объем в кредитах и часах;
  * номер и название дисциплин(метадисциплин);
  * объем дисциплины в академических часах.
В форме редактирования дисциплин(метадисциплин) вводятся:
  * выбираются из справочника-банка учебных дисциплин(Discipline) дисциплины, которые в списке идентифицируются названием, количеством модулей, контрольными мероприятиями и ФИО преподавателя, разработавшего её (для обязательных дисциплин(метадисциплин) выбирается одна учебная дисциплина, для выборочных дисциплин(метадисциплин) возможен ввод нескольких учебных дисциплин);
  * указываются семестры чтения дисциплины;
  * указываются семестры с формами контроля экзамен, зачёт, контрольная работа;
  * указываются количество лекционных часов, часов лабораторных работ и часов практических занятий, при этом вычисляется и отображается процент аудиторных часов от общего количества часов дисциплины.