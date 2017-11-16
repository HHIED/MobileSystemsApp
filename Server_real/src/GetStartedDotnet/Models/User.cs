using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace GetStartedDotnet.Models
{
    public class User
    {
        [Key]
        public int Id { get; set; }


        public User(int Id)
        {
            this.Id = Id;
        }

        public User()
        {

        }
    }
}
