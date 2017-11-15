using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;
using GetStartedDotnet.Models;
using System.Linq;
using System.Text.Encodings.Web;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace GetStartedDotnet.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : Controller
    {
        private readonly HtmlEncoder _htmlEncoder;
        private readonly VisitorsDbContext _dbContext;

        public UsersController(HtmlEncoder htmlEncoder, VisitorsDbContext dbContext = null)
        {
            _dbContext = dbContext;
            _htmlEncoder = htmlEncoder;
        }

        // GET: api/values
        [HttpGet]
        public IEnumerable<string> Get()
        {
            if (_dbContext == null)
            {
                return new string[] { "sadasd" };
            }
            else
            {
                return new string[] { _dbContext.Users.ToString() };
            }
        }

        // POST api/values
        [Route("create/{Id}")]
        [HttpPut("{Id}")]        
        public string Post(int Id)
        {
            if (_dbContext == null)
            {
                return "test";
            }
            else
            {
                User user = new User(Id);
                _dbContext.Users.Add(user);
                _dbContext.SaveChanges();
                return "test2";
            }
        }
    }
}
