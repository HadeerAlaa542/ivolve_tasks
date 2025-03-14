# terraform.tfvars

aws_region         = "us-east-1"
vpc_cidr           = "10.0.0.0/16"
public_subnet_cidr = "10.0.0.0/24"
private_subnet_cidr = "10.0.1.0/24"
ami_id             = "ami-08b5b3a93ed654d19" # Example AMI for Amazon Linux 2 in us-east-1
instance_type      = "t2.micro"
key_name           = "lab-key" # Replace with your SSH key pair name
