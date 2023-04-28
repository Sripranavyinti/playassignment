package controllers.api
import controllers.routes._

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{BaseForm, TodoFormData}

import play.api.data.FormError

import services.TodoService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class TodoController @Inject()(
                                cc: ControllerComponents,
                                todoService: TodoService
                              ) extends AbstractController(cc) {

  implicit val todoFormat = Json.format[BaseForm]

  def getAll = Action.async { implicit request: Request[AnyContent] =>
    todoService.listAllItems map { items =>
      Ok(Json.toJson(items))
    }
  }

  def getById(companyID: String,employeeID: String) = Action.async { implicit request: Request[AnyContent] =>
    todoService.getItem(companyID,employeeID) map { item =>
      Ok(Json.toJson(item))
    }
  }
  def getbycompany(companyID: String) = Action.async { implicit request: Request[AnyContent] =>
    todoService.getbycompany(companyID) map { item =>
      Ok(Json.toJson(item))
    }
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    TodoFormData.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val newTodoItem = BaseForm(data.id,data.employeeID , data.companyID, data.details)
        todoService.addItem(newTodoItem).map( _ => Redirect(routes.TodoController.getAll))
      })
  }

  def update(employeeID: String) = Action.async { implicit request: Request[AnyContent] =>
    TodoFormData.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val todoItem = BaseForm(data.id,employeeID,data.companyID, data.details)
        todoService.updateItem(todoItem).map( _ => Redirect(routes.TodoController.getAll))
      })
  }

  def delete(companyID: String,employeeID: String) = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteItem(companyID,employeeID) map { res =>
      Redirect(routes.TodoController.getAll)
    }
  }
}