package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._



//import com.google.inject.Inject
//import play.api.data.Form
//import play.api.data.Forms.{mapping,text}
//import play.api.data.Forms._
//import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
//import slick.jdbc.JdbcProfile
//import slick.lifted.TableQuery
//import slick.jdbc.MySQLProfile.api._

//import scala.concurrent.{ExecutionContext, Future}

case class BaseForm(id : String, employeeID: String, companyID: String, details:String)
//case class TodoFormData( companyID: String, details: String)

object TodoFormData {
  val form = Form(
    mapping(
      "id" -> text,
      "employeeID" -> text,
      "companyID" -> text,
      "details" -> text

    )(BaseForm.apply)(BaseForm.unapply)
  )
}

class BaseFormDef (tag: Tag) extends Table[BaseForm](tag, "baseForm"){
  def id= column[String]("id", O.PrimaryKey)
  def employeeID = column[String]("employeeID")
  def companyID = column[String]("companyID")
  def details = column[String]("details")


  override def * = (id,employeeID, companyID, details) <> (BaseForm.tupled, BaseForm.unapply)
}


class List @Inject()(
       protected val dbConfigProvider: DatabaseConfigProvider
         )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  var list = TableQuery[BaseFormDef]

  def add(todoItem: BaseForm): Future[String] = {
    dbConfig.db
      .run(list += todoItem)
      .map(res => "TodoItem successfully added")
      .recover {
        case ex: Exception => {
          printf(ex.getMessage())
          ex.getMessage
        }
      }
  }

  def delete(companyID: String,employeeID: String) = {
    dbConfig.db.run(list.filter(_.employeeID === employeeID).filter(_.companyID=== companyID).delete)
  }

  def update(todoItem: BaseForm) = {
    dbConfig.db
      .run(list.filter(_.employeeID === todoItem.employeeID).filter(_.companyID=== todoItem.companyID)
        .map(x => (x.companyID, x.details))
        .update(todoItem.companyID, todoItem.details)
      )
  }

  def get(companyID: String,employeeID: String): Future[Option[BaseForm]] = {
    dbConfig.db.run(list.filter(_.employeeID === employeeID).filter(_.companyID=== companyID).result.headOption)
  }
  def getbycompany(companyID:String) = {
    dbConfig.db.run(list.filter(_.companyID=== companyID).result)
  }

  def listAll: Future[Seq[BaseForm]] = {
    dbConfig.db.run(list.result)
  }
}
