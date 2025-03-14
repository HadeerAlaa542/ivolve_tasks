# outputs.tf

output "nginx_public_ip" {
  description = "Public IP of the NGINX EC2 instance"
  value       = aws_instance.nginx.public_ip
}

output "apache_private_ip" {
  description = "Private IP of the Apache EC2 instance"
  value       = aws_instance.apache.private_ip
}
