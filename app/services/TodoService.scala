package services
import com.google.inject.Inject
import models.{BaseForm, List}

import scala.concurrent.Future

class TodoService @Inject() (items: List) {

  def addItem(item: BaseForm): Future[String] = {
    items.add(item)
  }

  def deleteItem(companyID : String , employeeID: String): Future[Int] = {
    items.delete(companyID, employeeID)
  }

  def updateItem(item: BaseForm): Future[Int] = {
    items.update(item)
  }

  def getItem(companyID: String,employeeID: String): Future[Option[BaseForm]] = {
    items.get(companyID,employeeID)
  }
  def getbycompany(companyID: String) = {
    items.getbycompany(companyID)
  }

  def listAllItems: Future[Seq[BaseForm]] = {
    items.listAll
  }
}