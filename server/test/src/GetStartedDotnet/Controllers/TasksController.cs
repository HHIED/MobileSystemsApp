using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using System.Text.Encodings.Web;
using GetStartedDotnet.Models;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace GetStartedDotnet.Controllers
{
    [Route("api/[controller]")]
    public class TasksController : Controller
    {
        private readonly HtmlEncoder _htmlEncoder;
        private readonly VisitorsDbContext _dbContext;

        public TasksController(HtmlEncoder htmlEncoder, VisitorsDbContext dbContext = null)
        {
            _dbContext = dbContext;
            _htmlEncoder = htmlEncoder;
        }

        [HttpGet]
        public ActionResult GetAllTasks()
        {
            List<Models.Task> tasks = _dbContext.Tasks.ToList();
            return Json(tasks);
        }


    }
}
