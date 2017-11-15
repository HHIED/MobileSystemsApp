using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

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
    }
}