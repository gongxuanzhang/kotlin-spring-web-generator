package com.github.gongxuanzhang.kotlinspringwebgenerator.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import org.jetbrains.kotlin.psi.KtClass

class GeneratorKotlinCode : AnAction() {


    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun actionPerformed(event: AnActionEvent) {
        // Using the event, create and show a dialog
        val currentProject = event.project
        val message = StringBuilder(event.presentation.text + " Selected!")
        // If an element is selected in the editor, add info about it.
        val selectedElement = event.getData(CommonDataKeys.NAVIGATABLE)
        val psi = event.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val controllerString = controllerString()

//        if (psi is KtClass) {
//            psi.declarations.forEach { it.name }
//        }

        if (selectedElement != null) {
            message.append("\nSelected Element: ").append(selectedElement)
        }
        val title = event.presentation.description
        Messages.showMessageDialog(
            currentProject,
            message.toString(),
            title,
            Messages.getInformationIcon()
        )
    }

    private fun controllerString(): String {
        return """
            @RestController
            @RequestMapping("/equipment")
            open class EquipmentController(
                private val equipmentService: EquipmentService
            ) {

                /**
                 * 查询设备
                 */
                @GetMapping
                fun list(query: EquipmentQuery): ResultModel<List<Equipment>> {

                    return ResultModel.success(equipmentService.listEquipment(query))
                }

                /**
                 * 添加设备
                 */
                @PostMapping
                fun add(equipment: Equipment): ResultModel<Any> {

                    equipmentService.addEquipment(equipment)
                    return ResultModel.success()
                }

                /**
                 * 修改设备
                 */
                @PutMapping
                fun update(equipment: Equipment): ResultModel<Any> {

                    equipmentService.updateEquipment(equipment)
                    return ResultModel.success()
                }

                /**
                 * 修改设备
                 */
                @DeleteMapping
                fun delete(@RequestParam("ids") ids: List<String>): ResultModel<Any> {

                    equipmentService.deleteEquipment(ids)
                    return ResultModel.success()
                }
            }
        """.trimIndent()

    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.setEnabledAndVisible(project != null)
    }
}
